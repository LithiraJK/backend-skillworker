package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.entity.location.District;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdService {
    void createAd(AdRequestDTO adRequestDTO);

    List<AdResponseDTO> getAllAds();

//    List<AdResponseDTO> searchAdsByCategory(String keyword);

    void changeAdStatus(Long id, String adStatus);

    void updateAd(Long id, AdRequestDTO adRequestDTO);

    AdResponseDTO getAdById(Long id);

    void deleteAd(Long id);

    List<AdResponseDTO> getAdsByWorkerId(Long workerId);

    List<AdResponseDTO> getAdsByDistrict(District district);

    List<AdResponseDTO> getAdsByCategory(String category);

    Map<String, Object> getAllActiveAds(int page, int size);

    Map<String, Object> getAllActiveAdsByDistrict(District district, int page, int size);

    List<AdResponseDTO> getRecentAds();

    Map<String, Object> getAllActiveAdsByCategory(String category, int page, int size);

//    List<AdDetailResponseDTO> searchAllAdsByKeyword(String keyword);
}
