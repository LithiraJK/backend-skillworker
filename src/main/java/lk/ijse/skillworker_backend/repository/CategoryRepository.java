package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.worker.WorkerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Category> findByIsActiveTrue();


    @Query("SELECT c FROM Category c " +
           "JOIN WorkerCategory wc ON c.id = wc.category.id " +
           "WHERE wc.worker.id = :workerId AND c.isActive = true")
    List<Category> findActiveCategoriesByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT new lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO( " +
            "c.id, c.name, c.description, c.isActive , COUNT(a.id)) " +
            "FROM Category c " +
            "LEFT JOIN Ad a ON a.category = c " +
            "GROUP BY c.id, c.name, c.description")
    List<CategoryResponseDTO> findAllCategoriesWithAdsCount();
}
