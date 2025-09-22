package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.SubscriptionRequestDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionResponseDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionStatusResponseDTO;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.subscription.PaymentStatus;
import lk.ijse.skillworker_backend.entity.subscription.Subscription;
import lk.ijse.skillworker_backend.exception.ResourceNotFoundException;
import lk.ijse.skillworker_backend.repository.SubscriptionRepository;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static lk.ijse.skillworker_backend.entity.subscription.SubscriptionPlan.*;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Value("${sandbox.merchantId}")
    private String merchantId;

    @Value("${sandbox.merchantSecret}")
    private String merchantSecret;

    @Value("${sandbox.currency}")
    private String currency;

    @Override
    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO)  {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String orderId = "SW-" + UUID.randomUUID();

        BigDecimal amount = switch (requestDTO.getPlanType()) {
            case BASIC -> new BigDecimal("0.00");
            case PRO -> new BigDecimal("2499.00");
            case PREMIUM -> new BigDecimal("4999.00");
        };

        System.out.println("Amount: " + amount);

        Subscription subscription = Subscription.builder()
                .user(user)
                .subscriptionPlan(requestDTO.getPlanType())
                .amount(amount)
                .currency(currency)
                .orderId(orderId)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        subscriptionRepository.save(subscription);

        String hash = generateHash(merchantId, orderId, amount, currency, merchantSecret);

        System.out.println("Generated Hash: " + hash);
        return SubscriptionResponseDTO.builder()
                .orderId(orderId)
                .hash(hash)
                .amount(String.format("%.2f", amount))
                .merchantId(merchantId)
                .currency(currency)
                .planType(requestDTO.getPlanType().name())
                .build();
    }

    private String generateHash(String merchantId, String orderId, BigDecimal amount, String currency, String merchantSecret) {
        try {
            String amountStr = String.format("%.2f", amount);
            String hashedMerchantSecret = md5(merchantSecret).toUpperCase();

            System.out.println("Hash Generation - MerchantId: " + merchantId + ", OrderId: " + orderId + ", Amount: " + amountStr + ", Currency: " + currency);

            String hashString = merchantId + orderId + amountStr + currency + hashedMerchantSecret;
            System.out.println("Hash String: " + hashString);
            String finalHash = md5(hashString).toUpperCase();
            System.out.println("Final Hash: " + finalHash);
            return finalHash;
        } catch (Exception e) {
            throw new RuntimeException("Error generating PayHere hash", e);
        }
    }

    private String generateNotificationHash(String merchantId, String orderId, BigDecimal amount, String currency, String statusCode, String merchantSecret) {
        try {
            String amountStr = String.format("%.2f", amount);
            String hashedMerchantSecret = md5(merchantSecret).toUpperCase();

            String hashString = merchantId + orderId + amountStr + currency + statusCode + hashedMerchantSecret;
            return md5(hashString).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PayHere notification hash", e);
        }
    }


    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    @Override
    public void handlePayHereNotification(String orderId, String statusCode, String md5sig) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByOrderId(orderId);

        if (optionalSubscription.isEmpty()) {
            throw new ResourceNotFoundException("Subscription not found for orderId: " + orderId);
        }

        Subscription subscription = optionalSubscription.get();

        // Validate the notification hash
        String expectedHash = generateNotificationHash(merchantId, orderId, subscription.getAmount(), currency, statusCode, merchantSecret);
        if (!expectedHash.equalsIgnoreCase(md5sig)) {
            throw new RuntimeException("Invalid PayHere notification - hash mismatch");
        }

        if ("2".equals(statusCode)) { // PayHere "2" = Success
            subscription.setPaymentStatus(PaymentStatus.SUCCESS);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setEndDate(LocalDateTime.now().plusDays(30)); // 30 days validity
        } else {
            subscription.setPaymentStatus(PaymentStatus.FAILED);
        }
        subscriptionRepository.save(subscription);
    }

    @Override
    public SubscriptionStatusResponseDTO getSubscriptionStatus(Long userId) {
        return subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .map(subscription -> {
                    boolean active = subscription.getPaymentStatus() == PaymentStatus.SUCCESS &&
                            subscription.getStartDate() != null &&
                            subscription.getEndDate() != null &&
                            LocalDateTime.now().isAfter(subscription.getStartDate()) &&
                            LocalDateTime.now().isBefore(subscription.getEndDate());

                    return SubscriptionStatusResponseDTO.builder()
                            .active(active)
                            .plan(subscription.getSubscriptionPlan().name())
                            .expiresAt(subscription.getEndDate())
                            .build();
                })
                .orElse(SubscriptionStatusResponseDTO.builder()
                        .active(false)
                        .plan("FREE")
                        .expiresAt(null)
                        .build());
    }



}
