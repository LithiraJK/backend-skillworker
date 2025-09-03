package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.dto.response.AdResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findAllByWorkerId(Long workerId);
}
