package lk.ijse.skillworker_backend.config;

import lk.ijse.skillworker_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AuthService authService;

    @Override
    public void run(String... args) {
        String status = authService.createDefaultAdmin();
        System.out.println(status);
    }
}
