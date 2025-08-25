package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.dto.response.AdStatusUpdateDTO;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lk.ijse.skillworker_backend.repository.AdRepository;
import lk.ijse.skillworker_backend.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ad")
@CrossOrigin
public class AdController {

    private final AdService adService;


    @PostMapping("/create")
    public ResponseEntity<APIResponse<String>> createAd(@RequestBody AdRequestDTO adRequestDTO) {
        adService.createAd(adRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(200, "Ad created successfully", null), HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> getAllAds() {
        List<AdResponseDTO> ads = adService.getAllAds();
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

//    @GetMapping("/search/{keyword}")
//    public ResponseEntity<APIResponse<List<AdResponseDTO>>> searchAdsByCategory( @PathVariable String keyword) {
//        List<AdResponseDTO> ads = adService.searchAdsByCategory(keyword);
//        return  ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
//    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> changeStatus(@PathVariable Long id ,  @RequestBody AdStatusUpdateDTO requestDTO) {
        adService.changeAdStatus(id , requestDTO.getStatus());
        return ResponseEntity.ok(new APIResponse<>(200, "Ad status updated successfully", null));
    }
}
