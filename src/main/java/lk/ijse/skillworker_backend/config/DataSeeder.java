package lk.ijse.skillworker_backend.config;

import lk.ijse.skillworker_backend.service.AuthService;
import lk.ijse.skillworker_backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AuthService authService;
    private  final LocationService locationService;

    @Override
    public void run(String... args) {
        String status = authService.createDefaultAdmin();
        System.out.println(status);

        boolean isLocationCreated = locationService.createDefaultLocation();
        if (isLocationCreated) {
            System.out.println("Default locations created successfully.");
        } else {
            System.out.println("Default locations already exist.");
        }
    }
}
