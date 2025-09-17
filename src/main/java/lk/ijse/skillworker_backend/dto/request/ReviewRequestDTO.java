package lk.ijse.skillworker_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private String comment;
    private int rating;
    private Long reviewerId;  // User id
    private Long workerId;    // optional
    private Long adId;        // optional
}
