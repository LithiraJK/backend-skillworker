package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.ReviewRequestDTO;
import lk.ijse.skillworker_backend.dto.response.ReviewResponseDTO;
import lk.ijse.skillworker_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO requestDTO) {
        ReviewResponseDTO savedReview = reviewService.createReview(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ReviewResponseDTO>> getWorkerReviews(@PathVariable Long workerId) {
        return ResponseEntity.ok(reviewService.getWorkerReviews(workerId));
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<ReviewResponseDTO>> getAdReviews(@PathVariable Long adId) {
        return ResponseEntity.ok(reviewService.getAdReviews(adId));
    }

    @GetMapping("/worker/{workerId}/rating")
    public ResponseEntity<Double> getWorkerAverageRating(@PathVariable Long workerId) {
        return ResponseEntity.ok(reviewService.getWorkerAverageRating(workerId));
    }

    @GetMapping("/ad/{adId}/rating")
    public ResponseEntity<Double> getAdAverageRating(@PathVariable Long adId) {
        return ResponseEntity.ok(reviewService.getAdAverageRating(adId));
    }
}