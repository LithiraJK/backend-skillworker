package lk.ijse.skillworker_backend.controller;

import lk.ijse.skillworker_backend.dto.request.CategoryRequestDTO;
import lk.ijse.skillworker_backend.dto.response.APIResponse;
import lk.ijse.skillworker_backend.dto.response.CategoryResponseDTO;
import lk.ijse.skillworker_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<String>> createCategory(@RequestBody CategoryRequestDTO categoryDTO) {

        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(new APIResponse<>(201, "Category created successfully", null), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("/getall")
    public ResponseEntity<APIResponse<List<CategoryResponseDTO>>> getCategories() {

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new APIResponse<>(200, "Category list fetch successfully", categories));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> updateCategory(@PathVariable Long id,  @RequestBody CategoryRequestDTO categoryDTO) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(new APIResponse<>(200, "Category updated successfully", updatedCategory));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("status/{id}")
    public ResponseEntity<APIResponse<String>> changeCategoryStatus(@PathVariable Long id) {
        categoryService.changeCategoryStatus(id);
        return ResponseEntity.ok(new APIResponse<>(200, "Category status changed successfully", null));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    @GetMapping("search/{keyword}")
    public ResponseEntity<APIResponse<List<CategoryResponseDTO>>> searchCategory(@PathVariable String keyword) {
        List<CategoryResponseDTO> categories = categoryService.searchCategory(keyword);

        return ResponseEntity.ok(new APIResponse<>( 200, "Category search results", categories));
    }










}
