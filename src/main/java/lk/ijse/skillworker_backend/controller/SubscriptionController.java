package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.SubscriptionRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
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


    @PostMapping("/payhere/notify")
    public ResponseEntity<APIResponse<String>> handlePayHereNotification(
            @RequestParam String order_id,
            @RequestParam String status_code,
            @RequestParam String md5sig) {

        subscriptionService.handlePayHereNotification(order_id, status_code, md5sig);
        return ResponseEntity.ok(new APIResponse<>(200, "Notification processed successfully", null));
    }


    @PostMapping("/payhere/success")
    public ResponseEntity<APIResponse<String>> handlePayHereSuccess(){
        return ResponseEntity.ok(new APIResponse<>(200, "Payment Successful", null));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<SubscriptionStatusResponseDTO> getSubscriptionStatus(@PathVariable Long userId) {
        SubscriptionStatusResponseDTO status = subscriptionService.getSubscriptionStatus(userId);
        return ResponseEntity.ok(status);
    }





}
