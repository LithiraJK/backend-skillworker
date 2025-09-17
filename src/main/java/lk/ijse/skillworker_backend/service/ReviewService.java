package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.ReviewRequestDTO;
import lk.ijse.skillworker_backend.dto.response.ReviewResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    ReviewResponseDTO createReview(ReviewRequestDTO requestDTO);
    List<ReviewResponseDTO> getWorkerReviews(Long workerId);
//    List<ReviewResponseDTO> getAdReviews(Long adId);
    Double getWorkerAverageRating(Long workerId);
//    Double getAdAverageRating(Long adId);

    List<ReviewResponseDTO> getAllReviews();

    String approveStatus(Long id);

    String rejectStatus(Long id);
}
