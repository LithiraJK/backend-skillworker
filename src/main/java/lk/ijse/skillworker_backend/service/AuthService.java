package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.request.UserRequestDTO;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    AuthResponseDTO authenticate(AuthDTO authDTO);
    String register(RegisterDTO registerDTO);
    String createDefaultAdmin();
    AuthResponseDTO refreshToken(String refreshToken);

    UserResponseDTO getUser(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userDTO);

    List<UserResponseDTO> getAllUsers();

    void changeUserStatus(Long id);
}
