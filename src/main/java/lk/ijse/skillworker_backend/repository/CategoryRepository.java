package lk.ijse.skillworker_backend.repository;

import lk.ijse.skillworker_backend.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoryByNameContainingIgnoreCaseAndIsActive(String name, boolean active);

    boolean existsByNameIgnoreCase(String name);
}
