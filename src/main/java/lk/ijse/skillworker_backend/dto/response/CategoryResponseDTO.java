package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
}
