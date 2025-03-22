package com.project.backend.web.controller;

import com.project.backend.entities.Product;
import com.project.backend.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @Operation(summary = "Get all products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all products",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
            })
    @GetMapping
    public ResponseEntity<List<Product>> obterTodosProdutos() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping("/get/{id}")
    public ResponseEntity<Product> obterProdutoPorId(@PathVariable Long id) {
        Optional<Product> produto = productService.getProductById(id);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "Delete product",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
            })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        Optional<Product> produtoExistente = productService.getProductById(id);
        if (produtoExistente.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
