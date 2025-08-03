package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> registerUser (@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "User Registration Success !!",
                authService.register(registerDTO)));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDTO>> login (@RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "User Login Success !!",
                authService.authenticate(authDTO))
        );
    }
}

