package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.entity.worker.WorkerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Category> findByIsActiveTrue();

    List<WorkerCategory> findAllById(Long id);
}
