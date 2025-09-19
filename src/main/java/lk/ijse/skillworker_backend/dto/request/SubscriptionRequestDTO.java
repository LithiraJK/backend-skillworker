package lk.ijse.skillworker_backend.dto.request;

import lk.ijse.skillworker_backend.entity.subscription.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionRequestDTO {
    private Long userId;
    private SubscriptionPlan planType;
}
