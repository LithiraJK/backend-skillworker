package lk.ijse.skillworker_backend.service;

import lk.ijse.skillworker_backend.dto.request.CategoryRequestDTO;
import lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    void createCategory(CategoryRequestDTO categoryDTO);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDTO);

    void changeCategoryStatus(Long id);

    List<CategoryResponseDTO> searchCategory(String keyword);
}
