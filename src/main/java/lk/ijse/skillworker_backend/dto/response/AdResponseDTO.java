package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private String status;

    private String categoryName;
    private Long categoryId;
    private Long workerId;
}
