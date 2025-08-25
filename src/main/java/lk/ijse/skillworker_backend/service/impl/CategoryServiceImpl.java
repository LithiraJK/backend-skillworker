package lk.ijse.skillworker_backend.service.impl;

import lk.ijse.skillworker_backend.dto.request.CategoryRequestDTO;
import lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO;
import lk.ijse.skillworker_backend.entity.category.Category;
import lk.ijse.skillworker_backend.exception.CategoryNotFoundException;
import lk.ijse.skillworker_backend.exception.ResourceAlreadyExistsException;
import lk.ijse.skillworker_backend.repository.CategoryRepository;
import lk.ijse.skillworker_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createCategory(CategoryRequestDTO categoryDTO) {

        boolean exists = categoryRepository.existsByNameIgnoreCase(categoryDTO.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Category already exists with name: " + categoryDTO.getName()
            );
        }

        categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        System.out.println("Category saved successfully");

    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No categories found");
        }

        return modelMapper.map(categories, new TypeToken<List<CategoryResponseDTO>>() {}.getType());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return modelMapper.map(categoryRepository.save(category), CategoryResponseDTO.class);

    }

    @Override
    public void changeCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + id));
        category.setActive(!category.isActive());
        categoryRepository.save(category);

    }

    @Override
    public List<CategoryResponseDTO> searchCategory(String keyword) {
        List<Category> categories = categoryRepository.findCategoryByNameContainingIgnoreCaseAndIsActiveTrue(keyword);

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No active categories found for keyword: " + keyword);
        }
        return modelMapper.map(categories, new TypeToken<List<CategoryResponseDTO>>() {}.getType());
    }

    @Override
    public List<CategoryResponseDTO> getActiveCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No Active categories found");
        }

        return modelMapper.map(categories, new TypeToken<List<CategoryResponseDTO>>() {}.getType());
    }

}
