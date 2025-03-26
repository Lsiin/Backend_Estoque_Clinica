package com.project.backend.web.controller;

import com.project.backend.entities.Category;
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

    @Operation(summary = "Register a new category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/register")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(newCategory);
    }

    @Operation(summary = "Retrieve all categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Retrieve a category by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(mediaType = "application/json"))
            })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a category",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted successfully",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Category not found",
                            content = @Content(mediaType = "application/json"))
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}