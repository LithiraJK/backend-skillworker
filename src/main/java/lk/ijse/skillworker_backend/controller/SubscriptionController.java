package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.SubscriptionRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.PayhereResponseDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionResponseDTO;
import lk.ijse.skillworker_backend.dto.response.SubscriptionStatusResponseDTO;
import lk.ijse.skillworker_backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription")
@CrossOrigin
public class SubscriptionController {

    private  final SubscriptionService subscriptionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<SubscriptionResponseDTO>> createSubscription( @RequestBody SubscriptionRequestDTO requestDTO) {
        SubscriptionResponseDTO responseDTO = subscriptionService.createSubscription(requestDTO);
        return new ResponseEntity<>(new APIResponse<>(201,
                "Subscription created successfully",
                responseDTO),
                HttpStatus.CREATED);
    }


    @PostMapping("/notify")
    public ResponseEntity<APIResponse<String>> handlePayHereNotification(
            @RequestParam String order_id,
            @RequestParam String status_code,
            @RequestParam String md5sig) {

        System.out.println("Received PayHere notification: order_id=" + order_id + ", status_code=" + status_code + ", md5sig=" + md5sig);

        try {
            subscriptionService.handlePayHereNotification(order_id, status_code, md5sig);
            return ResponseEntity.ok(new APIResponse<>(200, "Notification processed successfully", null));
        } catch (Exception e) {
            System.err.println("PayHere notification error: " + e.getMessage());
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Error processing notification: " + e.getMessage(), null));
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @PostMapping("/payhere/success")
    public ResponseEntity<APIResponse<String>> handlePayHereSuccess(@RequestBody PayhereResponseDTO payhereResponse) {
        try {
            subscriptionService.handlePayHereNotification(payhereResponse.getOrderId(), payhereResponse.getStatusCode(), payhereResponse.getMd5sig());
            return ResponseEntity.ok(new APIResponse<>(200, "Subscription processed successfully", null));
        } catch (Exception e) {
            System.err.println("PayHere notification error: " + e.getMessage());
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Error processing Subscription process: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @GetMapping("/status/{userId}")
    public ResponseEntity<SubscriptionStatusResponseDTO> getSubscriptionStatus(@PathVariable Long userId) {
        SubscriptionStatusResponseDTO status = subscriptionService.getSubscriptionStatus(userId);
        return ResponseEntity.ok(status);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @GetMapping("/notification-hash/{orderId}/{statusCode}")
    public ResponseEntity<APIResponse<String>> getNotificationHash(@PathVariable String orderId, @PathVariable String statusCode) {
        try {
            String hash = subscriptionService.generateNotificationHashForOrder(orderId, statusCode);
            return ResponseEntity.ok(new APIResponse<>(200, "Hash generated successfully", hash));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Error generating hash: " + e.getMessage(), null));
        }
    }
}
