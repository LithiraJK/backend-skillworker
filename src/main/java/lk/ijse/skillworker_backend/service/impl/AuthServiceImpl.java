package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.AuthDTO;
import lk.ijse.skillworker_backend.dto.request.RegisterDTO;
import lk.ijse.skillworker_backend.dto.response.AuthResponseDTO;
import lk.ijse.skillworker_backend.entity.Role;
import lk.ijse.skillworker_backend.entity.User;
import lk.ijse.skillworker_backend.exception.ResourceAlreadyExistsException;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${admin.username}")
    private String admin_username;

    @Value("${admin.email}")
    private String admin_email;

    @Value("${admin.password}")
    private String admin_password;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user= userRepository
                .findByUsername(authDTO.getUsername()).orElseThrow(
                        ()->new UsernameNotFoundException
                                ("Username not found"));

        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String token=jwtUtil.generateToken(authDTO.getUsername());
        String username = user.getUsername();
        String role = user.getRole().name();
        return  new AuthResponseDTO(token, username, role);
    }

    public String register(RegisterDTO registerDTO) {

        if (registerDTO.getRole() != null && registerDTO.getRole().equals("ADMIN")) {
            throw new ResourceAlreadyExistsException("Cannot register as ADMIN");
        }

        if(userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        User user=  User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole()))
                .email(registerDTO.getEmail())
                .full_name(registerDTO.getFullName())
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
                .full_name("System Admin")
                .username(admin_username)
                .email(admin_email)
                .password(passwordEncoder.encode(admin_password))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
        return   "Default ADMIN created";

    }
}
