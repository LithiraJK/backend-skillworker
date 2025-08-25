package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.LocationRequestDTO;
import lk.ijse.skillworker_backend.dto.response.LocationResponseDTO;
import lk.ijse.skillworker_backend.entity.location.District;
import lk.ijse.skillworker_backend.entity.location.Location;
import lk.ijse.skillworker_backend.exception.ResourceAlreadyExistsException;
import lk.ijse.skillworker_backend.exception.ResourceNotFoundException;
import lk.ijse.skillworker_backend.repository.LocationRepository;
import lk.ijse.skillworker_backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createLocation(LocationRequestDTO locationDTO) {
        boolean exists = locationRepository.existsByDistrict(locationDTO.getDistrict());

        if (exists) {
            throw new ResourceAlreadyExistsException("Location already exists" + locationDTO.getDistrict());
        }

        locationRepository.save(modelMapper.map(locationDTO, Location.class));
        System.out.println("Location created successfully !");
    }

    @Override
    public List<LocationResponseDTO> getAllLocations() {

        List<Location> locations = locationRepository.findAll();

        if (locations.isEmpty()) {
            throw  new ResourceNotFoundException("No locations found");
        }
        return modelMapper.map(locations, new TypeToken<List<LocationResponseDTO>>() {}.getType());
    }

    @Override
    public void changeLocationStatus(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));

        location.setActive(!location.isActive());
        locationRepository.save(location);
        System.out.println("Location status changed successfully !");
    }

    @Override
    public List<LocationResponseDTO> searchLocations(String keyword) {

        List<District> matchingDistricts = Arrays.stream(District.values()) //enum eka array stream ekakata convert karanawa
                .filter(d -> d.name().toLowerCase() //district tika lowercase karanawa
                        .startsWith(keyword.toLowerCase())) //keyword ekath lowercase karla match karla blanawa
                .collect(Collectors.toList());  // match wena districts tika list ekakata collect karanawa

        if (matchingDistricts.isEmpty()) {
            throw new ResourceNotFoundException("No districts match keyword: " + keyword);
        }

        List<Location> locations = locationRepository.findByDistrictInAndIsActiveTrue(matchingDistricts);

        if (locations.isEmpty()) {
            throw new ResourceNotFoundException("No locations found for: " + keyword);
        }

        return modelMapper.map(locations, new TypeToken<List<LocationResponseDTO>>() {}.getType());
    }

    @Override
    public List<LocationResponseDTO> getActiveLocations() {
        List<Location> locations = locationRepository.findByIsActiveTrue();

        if (locations.isEmpty()) {
            throw  new ResourceNotFoundException("No locations found");
        }
        return modelMapper.map(locations, new TypeToken<List<LocationResponseDTO>>() {}.getType());
    }


}
