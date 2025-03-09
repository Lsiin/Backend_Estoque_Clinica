package com.project.backend.web.controller;

import com.project.backend.entities.Produto;
import com.project.backend.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProdutoController {

    @Autowired
    private ProductService productService;


    @PostMapping("/register")
    public ResponseEntity<Produto> registerProduct(@RequestBody Produto produto) {
        Produto novoProduto = productService.registerProduct(produto);
        return ResponseEntity.status(201).body(novoProduto);
    }


    @GetMapping
    public ResponseEntity<List<Produto>> obterTodosProdutos() {
        List<Produto> produtos = productService.getAllProducts();
        return ResponseEntity.ok(produtos);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Produto> obterProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produto = productService.getProductById(id);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Optional<Produto> produtoExistente = productService.getProductById(id);
        if (produtoExistente.isPresent()) {
            produto.setId(id);
            Produto produtoAtualizado = productService.updateProduct(produto);
            return ResponseEntity.ok(produtoAtualizado);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        Optional<Produto> produtoExistente = productService.getProductById(id);
        if (produtoExistente.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
