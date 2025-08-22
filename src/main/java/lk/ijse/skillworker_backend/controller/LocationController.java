package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.LocationRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.LocationResponseDTO;
import lk.ijse.skillworker_backend.service.LocationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@CrossOrigin
public class LocationController {

    private  final LocationService locationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<String>> createLocation(@RequestBody LocationRequestDTO locationRequestDTO) {
        locationService.createLocation(locationRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(201 , "Location created successfully", null), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<LocationResponseDTO>>> getAllLocations() {
        List<LocationResponseDTO> locations = locationService.getAllLocations();
        return ResponseEntity.ok(new APIResponse<>( 200, "Locations found", locations));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("status/{id}")
    public ResponseEntity<APIResponse<String>>  changeLocationStatus(@PathVariable Long id) {
        locationService.changeLocationStatus(id);
        return ResponseEntity.ok(new APIResponse<>( 200, "Location status updated successfully", null));
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<APIResponse<List<LocationResponseDTO>>> searchLocations(@PathVariable String keyword) {
        List<LocationResponseDTO> locations = locationService.searchLocations(keyword);
        return ResponseEntity.ok(new APIResponse<>( 200, "Locations found", locations));
    }

}
