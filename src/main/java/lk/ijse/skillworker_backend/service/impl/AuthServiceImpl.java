package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.entity.Role;
import lk.ijse.skillworker_backend.entity.User;
import lk.ijse.skillworker_backend.exception.EmailNotFoundException;
import lk.ijse.skillworker_backend.exception.ResourceAlreadyExistsException;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${admin.email}")
    private String admin_email;

    @Value("${admin.password}")
    private String admin_password;

    @Override
    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user= userRepository
                .findByEmail(authDTO.getEmail()).orElseThrow(
                        ()->new EmailNotFoundException
                                ("Email not found"));

        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String token=jwtUtil.generateToken(authDTO.getEmail());
        String refreshToken=jwtUtil.generateRefreshToken(authDTO.getEmail());


        String firstName = user.getFirstName() ;
        String role = user.getRole().name();
        return  new AuthResponseDTO(token,refreshToken, firstName, role);
    }
    @Override
    public String register(RegisterDTO registerDTO) {

        if (registerDTO.getRole() != null && registerDTO.getRole().equals("ADMIN")) {
            throw new ResourceAlreadyExistsException("Cannot register as ADMIN");
        }

        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent()){
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user=  User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .build();
        userRepository.save(user);
        return  "User Registration Success";
    }

    @Override
    public String createDefaultAdmin() {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return "ADMIN already exists";
        }
        User admin = User.builder()
                .firstName("Admin")
                .lastName("User")
                .email(admin_email)
                .password(passwordEncoder.encode(admin_password))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
        return   "Default ADMIN created";
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String email = jwtUtil.extractUserEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newAccessToken = jwtUtil.generateToken(email);

        return new AuthResponseDTO(newAccessToken, refreshToken, user.getFirstName(), user.getRole().name());
    }

}
