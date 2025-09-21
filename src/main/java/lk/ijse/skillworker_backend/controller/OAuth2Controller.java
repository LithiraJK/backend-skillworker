package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.entity.auth.Role;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@CrossOrigin
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/success")
    public ResponseEntity<AuthResponseDTO> oauth2Success(@AuthenticationPrincipal OAuth2User oauthUser) {
        if (oauthUser == null) {
            throw new IllegalStateException("OAuth2 authentication failed - user information not available");
        }

        String email = oauthUser.getAttribute("email");
        String fullName = oauthUser.getAttribute("name");

        if (email == null) {
            throw new IllegalStateException("Email not provided by OAuth2 provider");
        }

        String[] nameParts = fullName != null ? fullName.split(" ", 2) : new String[]{"Google", "User"};

        String firstName = nameParts[0];
        String lastName = (nameParts.length > 1) ? nameParts[1] : "";

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {

            user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .password("GOOGLE_LOGIN")
                    .role(Role.CLIENT)
                    .isActive(true)
                    .build();

            userRepository.save(user);
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        AuthResponseDTO response = new AuthResponseDTO(
                user.getId(),
                accessToken,
                refreshToken,
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}
