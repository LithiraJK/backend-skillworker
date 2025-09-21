package lk.ijse.skillworker_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.entity.auth.Role;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> registerUser (@Valid @RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "User Registration Success !!",
                authService.register(registerDTO)));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDTO>> login (@Valid @RequestBody AuthDTO authDTO) {
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

    @PostMapping("/oauth-register")
    public ResponseEntity<APIResponse<AuthResponseDTO>> oauthRegister(@RequestBody Map<String, String> oauthData) {
        String email = oauthData.get("email");
        String firstName = oauthData.get("firstName");
        String lastName = oauthData.get("lastName");
        String roleStr = oauthData.get("role");

        if (email == null || firstName == null || roleStr == null) {
            return ResponseEntity.badRequest()
                    .body(new APIResponse<>(400, "Missing required fields", null));
        }

        try {
            Role role = Role.valueOf(roleStr.toUpperCase());

            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new APIResponse<>(400, "User already exists", null));
            }

            // Create new OAuth user
            User newUser = User.builder()
                    .firstName(firstName)
                    .lastName(lastName != null ? lastName : "")
                    .email(email)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .role(role)
                    .isActive(true)
                    .build();

            User savedUser = userRepository.save(newUser);

            // Generate JWT tokens
            String accessToken = jwtUtil.generateToken(savedUser.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(savedUser.getEmail());

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUserId(savedUser.getId());
            authResponse.setToken(accessToken);
            authResponse.setRefreshToken(refreshToken);
            authResponse.setRole(savedUser.getRole().toString());

            return ResponseEntity.ok(new APIResponse<>(
                    200,
                    "OAuth user registration successful",
                    authResponse
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new APIResponse<>(500, "Registration failed: " + e.getMessage(), null));
        }
    }
}
