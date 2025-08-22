package lk.ijse.skillworker_backend.dto.response;

import lk.ijse.skillworker_backend.entity.location.District;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationResponseDTO {
    private Long location_id;
    private District district;
    private boolean isActive ;
}
