package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.ReviewRequestDTO;
import lk.ijse.skillworker_backend.dto.response.ReviewResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.auth.User;
import lk.ijse.skillworker_backend.entity.review.Review;
import lk.ijse.skillworker_backend.entity.review.ReviewStatus;
import lk.ijse.skillworker_backend.entity.worker.Worker;
import lk.ijse.skillworker_backend.exception.ResourceNotFoundException;
import lk.ijse.skillworker_backend.repository.AdRepository;
import lk.ijse.skillworker_backend.repository.ReviewRepository;
import lk.ijse.skillworker_backend.repository.UserRepository;
import lk.ijse.skillworker_backend.repository.WorkerRepository;
import lk.ijse.skillworker_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final AdRepository adRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {
        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));


        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setReviewer(reviewer);
        review.setCreatedAt(LocalDateTime.now());

        if (dto.getWorkerId() != null) {
            Worker worker = workerRepository.findById(dto.getWorkerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Worker not found"));

            if(dto.getReviewerId().equals(dto.getWorkerId())){
                throw new IllegalArgumentException("You cannot review yourself");
            }
            review.setWorker(worker);
        }

//        if (dto.getAdId() != null) {
//            Ad ad = adRepository.findById(dto.getAdId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
//
//            if (ad.getWorker().getId().equals(dto.getAdId())) {
//                throw new IllegalArgumentException("You cannot review your own ad");
//            }
//            review.setAd(ad);
//        }

        Review saved = reviewRepository.save(review);
        return new ReviewResponseDTO(
                saved.getId(),
                saved.getComment(),
                saved.getRating(),
                saved.getReviewer().getFirstName(),
                saved.getWorker().getUser().getFirstName(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponseDTO> getWorkerReviews(Long workerId) {
        return reviewRepository.findByWorkerId(workerId).stream()
                .map(r -> new ReviewResponseDTO(
                        r.getId(),
                        r.getComment(),
                        r.getRating(),
                        r.getReviewer().getFirstName(),
                        r.getWorker().getUser().getFirstName(),
                        r.getStatus(),
                        r.getCreatedAt()))
                .toList();
    }

//    @Override
//    public List<ReviewResponseDTO> getAdReviews(Long adId) {
//        return reviewRepository.findByAdId(adId).stream()
//                .map(r -> new ReviewResponseDTO(
//                        r.getId(),
//                        r.getComment(),
//                        r.getRating(),
//                        r.getReviewer().getFirstName(),
//                        r.getWorker().getUser().getFirstName(),
//                        r.getStatus(),
//                        r.getCreatedAt()))
//                .toList();
//    }

    @Override
    public Double getWorkerAverageRating(Long workerId) {
        return reviewRepository.findWorkerAverageRating(workerId);
    }

//    @Override
//    public Double getAdAverageRating(Long adId) {
//        return reviewRepository.findAdAverageRating(adId);
//    }

    @Override
    public List<ReviewResponseDTO> getAllReviews() {

        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found");
        }

        return reviews.stream()
                .map(review -> new ReviewResponseDTO(
                        review.getId(),
                        review.getComment(),
                        review.getRating(),
                        review.getReviewer().getFirstName(),
                        review.getWorker().getUser().getFirstName(),
                        review.getStatus(),
                        review.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public String approveStatus(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setStatus(ReviewStatus.APPROVED);
        reviewRepository.save(review);
        return "Review status changed successfully !";
    }

    @Override
    public String rejectStatus(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setStatus(ReviewStatus.REJECTED);
        reviewRepository.save(review);
        return "Review status changed successfully !";
    }
}
