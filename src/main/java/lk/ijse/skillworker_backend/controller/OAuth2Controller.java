package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.entity.auth.Role;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@CrossOrigin
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/success")
    public RedirectView oauth2Success(@AuthenticationPrincipal OAuth2User oauthUser, HttpServletRequest request) {
        System.out.println(request.getParameter("role"));
        try {
            if (oauthUser == null) {
                return new RedirectView("http://localhost:5500/pages/login-page.html?error=authentication_failed");
            }

            String email = oauthUser.getAttribute("email");
            String fullName = oauthUser.getAttribute("name");

            if (email == null) {
                return new RedirectView("http://localhost:5500/pages/login-page.html?error=email_not_provided");
            }

            String[] nameParts = fullName != null ? fullName.split(" ", 2) : new String[]{"Google", "User"};
            String firstName = nameParts[0];
            String lastName = (nameParts.length > 1) ? nameParts[1] : "";

            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!user.isActive()) {
                    user.setActive(true);
                    userRepository.save(user);
                }

                // Generate JWT tokens
                String accessToken = jwtUtil.generateToken(user.getEmail());
                String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

                // Redirect to frontend with tokens
                String redirectUrl = String.format(
                        "http://localhost:5500/pages/login-page.html?token=%s&refreshToken=%s&userId=%d&role=%s",
                        accessToken, refreshToken, user.getId(), user.getRole().toString()
                );
                return new RedirectView(redirectUrl);
            } else {
                String roleParam = null;

                roleParam = request.getParameter("role");

                if (roleParam == null || roleParam.isEmpty()) {
                    jakarta.servlet.http.Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (jakarta.servlet.http.Cookie cookie : cookies) {
                            if ("pendingOAuthRole".equals(cookie.getName())) {
                                roleParam = cookie.getValue();
                                break;
                            }
                        }
                    }
                }

                if (roleParam == null || roleParam.isEmpty()) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        roleParam = (String) session.getAttribute("pendingOAuthRole");
                    }
                }

                if (roleParam != null && !roleParam.isEmpty()) {
                    Role role;
                    try {
                        role = Role.valueOf(roleParam.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        role = Role.CLIENT;
                    }

                    User newUser = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .role(role)
                            .isActive(true)
                            .build();

                    User savedUser = userRepository.save(newUser);

                    String accessToken = jwtUtil.generateToken(savedUser.getEmail());
                    String refreshToken = jwtUtil.generateRefreshToken(savedUser.getEmail());

                    String redirectUrl = String.format(
                            "http://localhost:5500/pages/login-page.html?token=%s&refreshToken=%s&userId=%d&role=%s&clearOAuthRole=true",
                            accessToken, refreshToken, savedUser.getId(), savedUser.getRole().toString()
                    );
                    return new RedirectView(redirectUrl);
                } else {
                    String redirectUrl = String.format(
                            "http://localhost:5500/pages/signup-role-selection.html?oauth=true&email=%s&firstName=%s&lastName=%s",
                            email, firstName, lastName
                    );
                    return new RedirectView(redirectUrl);
                }
            }

        } catch (Exception e) {
            return new RedirectView("http://localhost:5500/pages/login-page.html?error=authentication_error");
        }
    }

    @GetMapping("/failure")
    public RedirectView oauth2Failure() {
        return new RedirectView("http://localhost:5500/pages/login-page.html?error=oauth_failure");
    }

    @PostMapping("/prepare")
    public ResponseEntity<String> prepareOAuth(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String role = request.get("role");
        if (role != null && !role.isEmpty()) {
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("pendingOAuthRole", role.toUpperCase());
            return ResponseEntity.ok("Role stored for OAuth");
        }
        return ResponseEntity.badRequest().body("Role required");
    }
}
