package lk.ijse.skillworker_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkerUpdateDTO {

    private Integer experienceYears;
    private String bio;
    private List<String> phoneNumbers;
    private List<String> skills;
    private String profilePictureUrl;
    private List<Long> categoryIds;
    private List<Long> locationIds;
}

