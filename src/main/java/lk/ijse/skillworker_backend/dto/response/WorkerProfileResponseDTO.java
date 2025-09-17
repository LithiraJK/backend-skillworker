package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WorkerProfileResponseDTO {
    private Long workerId;
    private String firstName;
    private String lastName;
    private  String email;

    private Integer experienceYears;

    private String bio;
    private String profilePictureUrl;
    private Double averageRating;
    private Long totalReviews;

    private List<String> skills;
    private List<String> phoneNumbers;
    private List<CategoryResponseDTO> categories;
    private List<LocationResponseDTO> locations;
}
