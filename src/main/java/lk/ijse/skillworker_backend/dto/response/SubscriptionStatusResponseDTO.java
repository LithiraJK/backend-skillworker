package lk.ijse.skillworker_backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionStatusResponseDTO {
    private boolean active;
    private String plan;
    private LocalDateTime expiresAt;
}