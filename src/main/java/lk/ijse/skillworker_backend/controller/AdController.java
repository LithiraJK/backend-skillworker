package lk.ijse.skillworker_backend.controller;

import jakarta.validation.Valid;
import lk.ijse.skillworker_backend.dto.request.AdRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO;
import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.dto.response.AdStatusUpdateDTO;
import lk.ijse.skillworker_backend.entity.location.District;
import lk.ijse.skillworker_backend.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ad")
@CrossOrigin
public class AdController {

    private final AdService adService;


    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<String>> createAd(@Valid @RequestBody AdRequestDTO adRequestDTO) {
        adService.createAd(adRequestDTO);
        return new ResponseEntity<>(new APIResponse<>(200, "Ad created successfully", null), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> getAllAds() {
        List<AdResponseDTO> ads = adService.getAllAds();
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getbyworker/{workerId}")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> getAdByWorkerId(@PathVariable Long workerId) {
        List<AdResponseDTO> ads = adService.getAdsByWorkerId(workerId);
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<String>> updateAd(@PathVariable Long id, @Valid  @RequestBody AdRequestDTO adRequestDTO) {
        adService.updateAd(id, adRequestDTO);
        return ResponseEntity.ok(new APIResponse<>(200, "Ad updated successfully", null));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/get/{id}")
    public ResponseEntity<APIResponse<AdResponseDTO>> getAdById(@PathVariable Long id) {
        AdResponseDTO ad = adService.getAdById(id);
        return ResponseEntity.ok(new APIResponse<>(200, "Ad retrieved successfully", ad));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.ok(new APIResponse<>(200, "Ad deleted successfully", null));
    }

//    @GetMapping("/search/{keyword}")
//    public ResponseEntity<APIResponse<List<AdResponseDTO>>> searchAdsByKeyword( @PathVariable String keyword) {
//        List<AdResponseDTO> ads = adService.searchAdsByKeyword(keyword);
//        return  ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
//    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @PatchMapping("/status/{id}")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> changeStatus(@PathVariable Long id ,  @RequestBody AdStatusUpdateDTO requestDTO) {
        adService.changeAdStatus(id , requestDTO.getStatus());
        return ResponseEntity.ok(new APIResponse<>(200, "Ad status updated successfully", null));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/category/{category}")
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> getAdsByDistrict(@PathVariable String category) {
        List<AdResponseDTO> ads = adService.getAdsByCategory(category);
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getall/active" )
    public ResponseEntity<APIResponse<Map<String,Object>>> getAllActiveAds(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Map<String,Object> ads = adService.getAllActiveAds(page , size);
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getall/active/{district}" )
    public ResponseEntity<APIResponse<Map<String,Object>>> getAllActiveAdsByDistrict(@PathVariable District district, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Map<String,Object> ads = adService.getAllActiveAdsByDistrict(district, page, size);
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getads/active/{category}" )
    public ResponseEntity<APIResponse<Map<String,Object>>> getAllActiveAdsByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Map<String,Object> ads = adService.getAllActiveAdsByCategory(category, page, size);
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }


    @GetMapping("/getrecent" )
    public ResponseEntity<APIResponse<List<AdResponseDTO>>> getRecentAds() {
        List<AdResponseDTO> ads = adService.getRecentAds();
        return ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
    }

//    @GetMapping("/search/{keyword}")
//    public ResponseEntity<APIResponse<List<AdDetailResponseDTO>>> searchAdsByCategory(@PathVariable String keyword) {
//        List<AdDetailResponseDTO> ads = adService.searchAllAdsByKeyword(keyword);
//        return  ResponseEntity.ok(new APIResponse<>(200, "Ads retrieved successfully", ads));
//    }


}
