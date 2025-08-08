package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO authenticate(AuthDTO authDTO);
    String register(RegisterDTO registerDTO);
    String createDefaultAdmin();
//    JwtAuthResponse refreshToken(String accessToken);
}
