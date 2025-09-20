package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.dto.response.AdDetailResponseDTO;
import lk.ijse.skillworker_backend.entity.ad.Ad;
import lk.ijse.skillworker_backend.entity.ad.AdStatus;
import lk.ijse.skillworker_backend.entity.location.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
    Page<AdDetailResponseDTO> findAllAdDetails(Pageable pageable);


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
    Page<AdDetailResponseDTO> findAllActiveAdDetailsByDistrict(@Param("district") District district , Pageable pageable);

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
            "AND c.name = :categoryName")
    Page<AdDetailResponseDTO> findAllActiveAdDetailsByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    List<Ad> findAllByWorkerId(Long workerId);

    Collection<Object> findTop5ByStatusOrderByCreatedDateDesc(AdStatus status);
}
