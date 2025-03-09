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


    public Product addProduct(Product product) {
        Product newProduct = productRepository.save(product);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantidade(0);
        stock.setTipoMovimento("entrada");
        stock.setDataMovimento(java.time.LocalDate.now());

        estoqueRepository.save(stock);
        return newProduct;
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
