package com.project.backend.services;

import com.project.backend.entities.Stock;
import com.project.backend.entities.Product;
import com.project.backend.repositories.EstoqueRepository;
import com.project.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProductRepository productRepository;


    public Product saveProduct(Product product) {
        if (product.getPreco() == null || product.getQtdEstoque() == null) {
            throw new IllegalArgumentException("Preço e Quantidade não podem ser nulos");
        }
        return productRepository.save(product);
    }


    public Product registerProduct(Product product) {
        return productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }


    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }


    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
}
