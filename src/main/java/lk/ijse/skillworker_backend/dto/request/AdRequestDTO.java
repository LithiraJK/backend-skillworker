package lk.ijse.skillworker_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Starting price is required")
    private BigDecimal startingPrice;

    @NotBlank(message = "Status is required")
    private AdStatus status;

    @NotBlank(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Worker ID is required")
    private Long workerId;
}
