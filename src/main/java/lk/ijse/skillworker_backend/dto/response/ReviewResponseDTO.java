package lk.ijse.skillworker_backend.dto.response;

import lk.ijse.skillworker_backend.entity.review.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private String comment;
    private int rating;
    private String reviewerName;
    private String workerName; // optional
    private ReviewStatus status;
    private LocalDateTime createdAt;

}
