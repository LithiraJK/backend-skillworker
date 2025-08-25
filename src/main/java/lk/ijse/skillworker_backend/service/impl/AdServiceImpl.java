package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lk.ijse.skillworker_backend.entity.category.Category;
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

import java.util.List;

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

}
