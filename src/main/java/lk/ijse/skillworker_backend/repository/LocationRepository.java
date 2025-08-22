package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.location.District;
import lk.ijse.skillworker_backend.entity.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByDistrict(District district);

    List<Location> findByDistrictInAndIsActive(List<District> matchingDistricts, boolean b);
}
