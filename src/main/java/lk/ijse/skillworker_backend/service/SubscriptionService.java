package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.SubscriptionRequestDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionResponseDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionStatusResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionService {

    SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO);

    void handlePayHereNotification(String orderId, String statusCode, String md5sig);

    SubscriptionStatusResponseDTO getSubscriptionStatus(Long userId);
}
