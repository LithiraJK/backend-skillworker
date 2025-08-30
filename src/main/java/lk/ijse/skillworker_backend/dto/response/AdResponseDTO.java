package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private LocalDate createdAt;
    private String status;

    private String categoryName;
    private Long categoryId;
    private Long workerId;
}
