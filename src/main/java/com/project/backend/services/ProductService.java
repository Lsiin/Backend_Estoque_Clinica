package com.project.backend.services;

import com.project.backend.entities.Product;
import com.project.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;


    public Product saveProduct(Product product) {
        if (product.getPrice() == null || product.getQuantity() == null) {
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

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setCategory(productDetails.getCategory());
            product.setSupplier(productDetails.getSupplier());
            return productRepository.save(product);
        });
    }


    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
