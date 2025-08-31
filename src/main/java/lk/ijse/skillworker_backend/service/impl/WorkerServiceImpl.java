package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.WorkerRequestDTO;
import lk.ijse.skillworker_backend.dto.request.WorkerUpdateDTO;
import lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO;
import lk.ijse.skillworker_backend.dto.response.LocationResponseDTO;
import lk.ijse.skillworker_backend.dto.response.WorkerResponseDTO;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.location.Location;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lk.ijse.skillworker_backend.entity.worker.WorkerCategory;
import lk.ijse.skillworker_backend.entity.worker.WorkerLocation;
import lk.ijse.skillworker_backend.exception.ResourceAlreadyExistsException;
import lk.ijse.skillworker_backend.exception.ResourceNotFoundException;
import lk.ijse.skillworker_backend.repository.CategoryRepository;
import lk.ijse.skillworker_backend.repository.LocationRepository;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.repository.WorkerRepository;
import lk.ijse.skillworker_backend.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WorkerRepository workerRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String registerWorker(WorkerRequestDTO requestDTO) {

        boolean exists = workerRepository.existsByUserId(requestDTO.getUserId());

        if (exists) {
            throw new ResourceAlreadyExistsException("Worker profile already exists for this userID: " + requestDTO.getUserId());
        }

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Worker worker = Worker.builder()
                .user(user)
                .experienceYears(requestDTO.getExperienceYears())
                .phoneNumbers(requestDTO.getPhoneNumbers())
                .bio(requestDTO.getBio())
                .skills(requestDTO.getSkills())
                .profilePictureUrl(requestDTO.getProfilePictureUrl())
                .profileComplete(true)
                .isActive(true)
                .build();

        // Assign categories
        for (Long categoryId : requestDTO.getCategoryIds()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            WorkerCategory wc = WorkerCategory.builder()
                    .worker(worker)
                    .category(category)
                    .build();

            worker.getWorkerCategories().add(wc);
        }

        // Assign locations
        for (Long locationId : requestDTO.getLocationIds()) {
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

            WorkerLocation wl = WorkerLocation.builder()
                    .worker(worker)
                    .location(location)
                    .build();

            worker.getWorkerLocations().add(wl);
        }
        workerRepository.save(worker);
        return "Worker registered successfully";
    }

    @Override
    public List<WorkerResponseDTO> getAllWorkers() {
        List<Worker> allWorkers = workerRepository.findAll();

        if (allWorkers.isEmpty()) {
            throw new ResourceNotFoundException("No workers found");
        }

        return allWorkers.stream().map(worker -> {
            WorkerResponseDTO dto = modelMapper.map(worker, WorkerResponseDTO.class);

            // Map categories
            dto.setCategories(worker.getWorkerCategories().stream()
                    .map(wc -> modelMapper.map(wc.getCategory(), CategoryResponseDTO.class))
                    .toList());

            // Map locations
            dto.setLocations(worker.getWorkerLocations().stream()
                    .map(wl -> modelMapper.map(wl.getLocation(), LocationResponseDTO.class))
                    .toList());

            return dto;
        }).toList();
    }


    @Override
    public void changeStatus(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        worker.setActive(!worker.isActive());
        workerRepository.save(worker);

    }

    @Override
    public WorkerResponseDTO getWorkerById(Long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with id: " + id));

        WorkerResponseDTO response = modelMapper.map(worker, WorkerResponseDTO.class);

        // map categories
        response.setCategories(worker.getWorkerCategories().stream()
                .map(wc -> modelMapper.map(wc.getCategory(), CategoryResponseDTO.class))
                .toList());

        // map locations
        response.setLocations(worker.getWorkerLocations().stream()
                .map(wl -> modelMapper.map(wl.getLocation(), LocationResponseDTO.class))
                .toList());

        return response;
    }


    @Override
    @Transactional
    public WorkerResponseDTO updateWorker(Long id, WorkerUpdateDTO dto) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (dto.getExperienceYears() != null) {
            worker.setExperienceYears(dto.getExperienceYears());
        }

        if (dto.getBio() != null) {
            worker.setBio(dto.getBio());
        }

        if (dto.getPhoneNumbers() != null) {
            worker.setPhoneNumbers(dto.getPhoneNumbers());
        }

        if (dto.getSkills() != null) {
            worker.setSkills(dto.getSkills());
        }

        if (dto.getProfilePictureUrl() != null) {
            worker.setProfilePictureUrl(dto.getProfilePictureUrl());
        }

        // Update categories if provided
        if (dto.getCategoryIds() != null) {
            worker.getWorkerCategories().clear();
            dto.getCategoryIds().forEach(catId -> {
                Category category = categoryRepository.findById(catId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

                WorkerCategory wc = WorkerCategory.builder()
                        .worker(worker)
                        .category(category)
                        .build();

                worker.getWorkerCategories().add(wc);
            });
        }

        // Update locations if provided
        if (dto.getLocationIds() != null) {
            worker.getWorkerLocations().clear();
            dto.getLocationIds().forEach(locId -> {
                Location location = locationRepository.findById(locId)
                        .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

                WorkerLocation wl = WorkerLocation.builder()
                        .location(location)
                        .worker(worker)
                        .build();
                worker.getWorkerLocations().add(wl);
            });
        }

        Worker saved = workerRepository.save(worker);

        // Map back to WorkerResponseDTO with categories & locations
        WorkerResponseDTO response = modelMapper.map(saved, WorkerResponseDTO.class);
        response.setCategories(saved.getWorkerCategories().stream()
                .map(wc -> modelMapper.map(wc.getCategory(), CategoryResponseDTO.class))
                .toList());
        response.setLocations(saved.getWorkerLocations().stream()
                .map(wl -> modelMapper.map(wl.getLocation(), LocationResponseDTO.class))
                .toList());

        return response;
    }


}
