package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.LocationRequestDTO;
import lk.ijse.skillworker_backend.dto.response.LocationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    void createLocation(LocationRequestDTO locationRequestDTO);
    boolean createDefaultLocation();

    List<LocationResponseDTO> getAllLocations();

    void changeLocationStatus(Long id);

    List<LocationResponseDTO> searchLocations(String keyword);

    List<LocationResponseDTO> getActiveLocations();
}
