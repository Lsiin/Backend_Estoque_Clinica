package com.project.backend.web.controller;

import com.project.backend.entities.Category;
import com.project.backend.entities.User;
import com.project.backend.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "CategoryRegister",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category registed successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Action unaltorized",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })


    @PostMapping("/register")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(newCategory);
    }

    @Operation(summary = "GettAllCategories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category finded successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Action unaltorized",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "GetCategoriesById",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category finded successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "403", description = "Action unaltorized",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
