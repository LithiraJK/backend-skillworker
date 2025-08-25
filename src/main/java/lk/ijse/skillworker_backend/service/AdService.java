package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdService {
    void createAd(AdRequestDTO adRequestDTO);

    List<AdResponseDTO> getAllAds();

    List<AdResponseDTO> searchAdsByCategory(String keyword);

    void changeAdStatus(Long id, String adStatus);
}
