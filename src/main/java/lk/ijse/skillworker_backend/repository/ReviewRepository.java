package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.dto.response.ReviewResponseDTO;
import lk.ijse.skillworker_backend.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByWorkerId(Long workerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.worker.id = :workerId")
    Double findWorkerAverageRating(@Param("workerId") Long workerId);

//    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.ad.id = :adId")
//    Double findAdAverageRating(@Param("adId") Long adId);

}
