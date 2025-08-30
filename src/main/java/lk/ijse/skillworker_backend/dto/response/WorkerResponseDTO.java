package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkerResponseDTO {
    private Long workerId;
    private Integer experienceYears;
    private List<String> phoneNumbers;
    private String bio;
    private List<String> skills;
    private String profilePictureUrl;
    private boolean profileComplete;

    private List<CategoryResponseDTO> categories;
    private List<LocationResponseDTO> locations;

}
