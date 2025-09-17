package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.location.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
//    SELECT a FROM Ad a JOIN a.worker w JOIN w.workerLocations wl JOIN wl.location l WHERE l.district = ?

    @Query("SELECT a FROM Ad a " +
            "JOIN a.worker w " +
            "JOIN w.workerLocations wl " +
            "JOIN wl.location l " +
            "WHERE l.district = :district")
    List<Ad> findAdsByDistrict(@Param("district") District district);

    @Query("SELECT a FROM Ad a " +
            "JOIN a.worker w " +
            "JOIN w.workerCategories wc " +
            "JOIN wc.category c " +
            "WHERE c.name = :categoryName")
    List<Ad> findAdsByCategory(@Param("categoryName") String categoryName);


    @Query("SELECT new lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO( " +
            "a.id, a.title, a.description, a.startingPrice, a.createdDate, a.status, " +
            "c.name, l.district, w.profilePictureUrl) " +
            "FROM Ad a " +
            "JOIN a.worker w " +
            "JOIN w.workerCategories wc " +
            "JOIN wc.category c " +
            "JOIN w.workerLocations wl " +
            "JOIN wl.location l" +
            " WHERE a.status = 'ACTIVE' ")
    List<AdDetailResponseDTO> findAllAdDetails();


    @Query("SELECT new lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO( " +
            "a.id, a.title, a.description, a.startingPrice, a.createdDate, a.status, " +
            "c.name, l.district, w.profilePictureUrl) " +
            "FROM Ad a " +
            "JOIN a.worker w " +
            "JOIN w.workerCategories wc " +
            "JOIN wc.category c " +
            "JOIN w.workerLocations wl " +
            "JOIN wl.location l " +
            "WHERE a.status = 'ACTIVE' " +
            "AND (:district IS NULL OR l.district = :district)")
    List<AdDetailResponseDTO> findAllActiveAdDetailsByDistrict(@Param("district") District district);


    List<Ad> findAllByWorkerId(Long workerId);
}
