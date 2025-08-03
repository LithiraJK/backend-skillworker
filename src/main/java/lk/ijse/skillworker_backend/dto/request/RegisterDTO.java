package lk.ijse.skillworker_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String role;
}
