package lk.ijse.skillworker_backend.dto.response;

import lombok.*;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lk.ijse.skillworker_backend.entity.location.District;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdDetailResponseDTO {
    private Long adId;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private LocalDate createdDate;
    private String status;

    private String categoryName;
    private String location;

    private List<String> phoneNumbers;
    private List<String> skills;
    private String profilePictureUrl;

    public AdDetailResponseDTO(Long adId, String title, String description,
                               BigDecimal startingPrice, LocalDate createdDate, AdStatus status, String categoryName, District location, String profilePictureUrl) {
        this.adId = adId;
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.createdDate = createdDate;
        this.status = status.toString();
        this.categoryName = categoryName;
        this.location = location.toString();
        this.profilePictureUrl = profilePictureUrl;
    }
}
