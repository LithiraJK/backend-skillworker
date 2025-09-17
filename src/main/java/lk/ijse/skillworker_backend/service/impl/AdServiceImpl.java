package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.location.District;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lk.ijse.skillworker_backend.exception.ResourceNotFoundException;
import lk.ijse.skillworker_backend.repository.AdRepository;
import lk.ijse.skillworker_backend.repository.CategoryRepository;
import lk.ijse.skillworker_backend.repository.WorkerRepository;
import lk.ijse.skillworker_backend.service.AdService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final WorkerRepository workerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createAd(AdRequestDTO adRequestDTO) {

        Category category = categoryRepository.findById(adRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Worker worker = workerRepository.findById(adRequestDTO.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));


        Ad ad = Ad.builder()
                .title(adRequestDTO.getTitle())
                .description(adRequestDTO.getDescription())
                .startingPrice(adRequestDTO.getStartingPrice())
                .createdDate(LocalDate.now())
                .status(adRequestDTO.getStatus() != null ? adRequestDTO.getStatus() : AdStatus.PENDING)
                .category(category)
                .worker(worker)
                .build();

        adRepository.save(ad);
    }

    @Override
    public List<AdResponseDTO> getAllAds() {
        List<Ad> ads = adRepository.findAll();

        if ((ads.isEmpty())){
            throw  new ResourceNotFoundException("No ads found");
        }
        return modelMapper.map(ads, new TypeToken<List<AdResponseDTO>>() {}.getType());
    }

    @Override
    public List<AdResponseDTO> searchAdsByCategory(String keyword) {
        List<Ad> ads = adRepository.findAll();

        if ((ads.isEmpty())){
            throw  new ResourceNotFoundException("No ads found");
        }
        return modelMapper.map(ads, new TypeToken<List<AdResponseDTO>>() {}.getType());
    }

    @Override
    @Transactional
    public void changeAdStatus(Long id, String adStatus) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        try {
            AdStatus status = AdStatus.valueOf(adStatus.toUpperCase());
            ad.setStatus(status);

            adRepository.save(ad);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AdStatus value: " + adStatus);
        }
    }

    @Override
    @Transactional
    public void updateAd(Long id, AdRequestDTO adRequestDTO) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        Category category = categoryRepository.findById(adRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Worker worker = workerRepository.findById(adRequestDTO.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

        if (adRequestDTO.getTitle() != null) {
            ad.setTitle(adRequestDTO.getTitle());
        }
        if (adRequestDTO.getDescription() != null) {
            ad.setDescription(adRequestDTO.getDescription());
        }
        if (adRequestDTO.getStartingPrice() != null) {
            ad.setStartingPrice(adRequestDTO.getStartingPrice());
        }
        if (adRequestDTO.getCategoryId() != null) {
            ad.setCategory(category);
        }
        if (adRequestDTO.getWorkerId() != null) {
            ad.setWorker(worker);
        }

        adRepository.save(ad);

    }

    @Override
    public AdResponseDTO getAdById(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        return modelMapper.map(ad, AdResponseDTO.class);
    }

    @Override
    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        if (ad.getStatus() == AdStatus.PENDING){
            adRepository.delete(ad);
        }else {
            throw new IllegalStateException("Only ads with PENDING status can be deleted");
        }

    }

    @Override
    public List<AdResponseDTO> getAdsByWorkerId(Long workerId) {
        boolean exists = workerRepository.existsById(workerId);

        if (!exists){
            throw new ResourceNotFoundException("Worker not found with id: " + workerId);
        }

        List<Ad> ads = adRepository.findAllByWorkerId(workerId);
        return modelMapper.map(ads, new TypeToken<List<AdResponseDTO>>() {}.getType());

    }

    @Override
    public List<AdResponseDTO> getAdsByDistrict(District district) {
        List<Ad> ads = adRepository.findAdsByDistrict(district);
        if (ads.isEmpty()){
            throw  new ResourceNotFoundException("No ads found");
        }

        return  modelMapper.map(ads, new TypeToken<List<AdResponseDTO>>() {}.getType());
    }

    @Override
    public List<AdResponseDTO> getAdsByCategory(String category) {
        List<Ad> ads = adRepository.findAdsByCategory(category);
        if (ads.isEmpty()){
            throw  new ResourceNotFoundException("No ads found");
        }
        return  modelMapper.map(ads, new TypeToken<List<AdResponseDTO>>() {}.getType());
    }

    @Override
    public List<AdDetailResponseDTO> getAllActiveAds() {
        List<AdDetailResponseDTO> adDetails = adRepository.findAllAdDetails();

        if (adDetails.isEmpty()) {
            throw new ResourceNotFoundException("No ads found");
        }

        // Enrich each DTO with phoneNumbers and skills
        for (AdDetailResponseDTO dto : adDetails) {
            Ad ad = adRepository.findById(dto.getAdId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

            Worker worker = ad.getWorker();
            dto.setPhoneNumbers(worker.getPhoneNumbers());
            dto.setSkills(worker.getSkills());
        }

        return adDetails;
    }

    @Override
    public List<AdDetailResponseDTO> getAllActiveAdsByDistrict(District district) {
        List<AdDetailResponseDTO> adDetails = adRepository.findAllActiveAdDetailsByDistrict(district);

        if (adDetails.isEmpty()) {
            throw new ResourceNotFoundException("No Services found in district : " + district);
        }

        // Enrich each DTO with phoneNumbers and skills
        for (AdDetailResponseDTO dto : adDetails) {
            Ad ad = adRepository.findById(dto.getAdId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

            Worker worker = ad.getWorker();
            dto.setPhoneNumbers(worker.getPhoneNumbers());
            dto.setSkills(worker.getSkills());
        }

        return adDetails;
    }

    @Override
    public List<AdResponseDTO> getRecentAds() {
        Collection<Object> recentAds = adRepository.findTop5ByStatusOrderByCreatedDateDesc(AdStatus.ACTIVE);

        if (recentAds.isEmpty()) {
            throw new ResourceNotFoundException("No recent ads found");
        }

        return recentAds.stream()
                .map(ad -> modelMapper.map(ad, AdResponseDTO.class))
                .collect(Collectors.toList());
    }


}
