package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository< Worker, Long> {

    boolean existsByUserId(Long userId);
}
