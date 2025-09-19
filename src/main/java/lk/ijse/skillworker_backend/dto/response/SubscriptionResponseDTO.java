package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SubscriptionResponseDTO {
    private String orderId;
    private String hash;
    private String amount;
    private String merchantId;
    private String currency;
    private String planType;
}
