package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
@CrossOrigin
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','CLIENT')")
    @PostMapping("/upload")
    public ResponseEntity<APIResponse<String>> uploadProfilePicture(@RequestParam("profilePic") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            System.out.println("Image URL : " +imageUrl);
            return ResponseEntity.ok(new APIResponse<>(200 , "Image Upload Success !" , imageUrl));
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(400 , "Image Upload Failed" , e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
