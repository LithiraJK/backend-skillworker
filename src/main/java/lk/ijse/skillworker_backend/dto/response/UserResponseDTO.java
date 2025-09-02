package lk.ijse.skillworker_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private String email;
    private  String firstName;
    private  String lastName;
}
