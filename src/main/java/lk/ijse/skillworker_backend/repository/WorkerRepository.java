package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository< Worker, Long> {

    boolean existsByUserId(Long userId);

    @Query("SELECT w FROM Worker w WHERE w.isActive = true AND w.profileComplete = true ORDER BY w.avgRating DESC LIMIT 3")
    List<Worker> findTop3ByAvgRatingDesc();
}
