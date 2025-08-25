package lk.ijse.skillworker_backend.dto.request;

import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdRequestDTO {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private AdStatus status;
    private Long categoryId;
    private Long workerId;
}
