package com.project.backend.web.controller;

import com.project.backend.entities.Product;
import com.project.backend.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;



    @GetMapping
    public ResponseEntity<List<Product>> obterTodosProdutos() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Product> obterProdutoPorId(@PathVariable Long id) {
        Optional<Product> produto = productService.getProductById(id);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



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
