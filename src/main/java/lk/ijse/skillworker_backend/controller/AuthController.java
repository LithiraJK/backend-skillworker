package lk.ijse.skillworker_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<AuthResponseDTO>> refreshAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader); // For debugging
        System.out.println(request);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String refreshToken = authHeader.substring(7);
        System.out.println("Refresh Token: " + refreshToken); // For debugging

        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Token Refreshed",
                authService.refreshToken(refreshToken))
        );
    }
}

