package lk.ijse.skillworker_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkerRequestDTO {

    private Long userId;
    private Integer experienceYears;
    private List<String> phoneNumbers;
    private String bio;
    private List<String> skills;
    private String profilePictureUrl;

    private List<Long> categoryIds;            // Category IDs (many-to-many)
    private List<Long> locationIds;            // Location IDs (many-to-many)
}
