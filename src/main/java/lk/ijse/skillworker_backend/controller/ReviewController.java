package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.ReviewRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.ReviewResponseDTO;
import lk.ijse.skillworker_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.util.PublicSuffixList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<ReviewResponseDTO>> createReview(@RequestBody ReviewRequestDTO requestDTO) {
        return new ResponseEntity<>(new APIResponse<>(
                201 ,
                "Review created successfully",
                reviewService.createReview(requestDTO)),
                HttpStatus.CREATED);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<APIResponse<List<ReviewResponseDTO>>> getWorkerReviews(@PathVariable Long workerId) {
        return ResponseEntity.ok(new APIResponse<>(
                200 ,
                "Worker Reviews fetch successfully" ,
                reviewService.getWorkerReviews(workerId))
        );
    }

//    @GetMapping("/ad/{adId}")
//    public ResponseEntity<APIResponse<List<ReviewResponseDTO>>> getAdReviews(@PathVariable Long adId) {
//        return ResponseEntity.ok(new APIResponse<>(200 ,
//                "Ad Reviews fetch Successfully !" ,
//                reviewService.getAdReviews(adId))
//        );
//    }

    @GetMapping("/worker/{workerId}/rating")
    public ResponseEntity<APIResponse<Double>> getWorkerAverageRating(@PathVariable Long workerId) {
        return ResponseEntity.ok(new APIResponse<>(200 ,
                "Success " ,
                reviewService.getWorkerAverageRating(workerId))
        );
    }

//    @GetMapping("/ad/{adId}/rating")
//    public ResponseEntity<APIResponse<Double>> getAdAverageRating(@PathVariable Long adId) {
//        return ResponseEntity.ok( new APIResponse<>(200 ,
//                "Success " ,
//                reviewService.getAdAverageRating(adId))
//        );
//    }

    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<ReviewResponseDTO>>> getAllReviews() {
        return ResponseEntity.ok(new APIResponse<>(200 ,
                "Success " ,
                reviewService.getAllReviews())
        );
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<APIResponse<String>> approveReviewStatus(@PathVariable Long id){
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Status Changed Successfully",
                reviewService.approveStatus(id))
        );
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<APIResponse<String>> rejectReviewStatus(@PathVariable Long id){
        return ResponseEntity.ok(new APIResponse<>(
                200,
                "Status Changed Successfully",
                reviewService.rejectStatus(id))
        );
    }

}