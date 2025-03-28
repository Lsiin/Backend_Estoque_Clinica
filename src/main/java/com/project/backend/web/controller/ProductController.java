package com.project.backend.web.controller;

import com.project.backend.dto.ProductDTO;
import com.project.backend.entities.Product;
import com.project.backend.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products",
            description = "Retrieves a list of all products.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
            })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID",
            description = "Retrieves a product by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new product",
            description = "Registers a new product in the system.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        Product newProduct = productService.saveProduct(productDTO);
        return ResponseEntity.status(201).body(newProduct);
    }


    // ===== Novo Endpoint para Upload de Planilhas =====
    @Operation(summary = "Upload Excel sheet for product data",
            description = "Processes an Excel file to add or update product data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File processed successfully"),
                    @ApiResponse(responseCode = "500", description = "Error processing the file")
            })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadProductSheet(@RequestParam("file") MultipartFile file) {
        try {
            productService.processProductSheet(file.getInputStream());
            return ResponseEntity.ok("Planilha processada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar a planilha: " + e.getMessage());
        }
    }


    @Operation(summary = "Update a product",
            description = "Updates an existing product by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {

        Product updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }



    @Operation(summary = "Delete a product by ID",
            description = "Deletes a product from the system based on the provided ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    }

