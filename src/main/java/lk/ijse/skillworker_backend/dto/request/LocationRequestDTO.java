package lk.ijse.skillworker_backend.dto.request;
import lk.ijse.skillworker_backend.entity.location.District;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationRequestDTO {
    private District district;
}
