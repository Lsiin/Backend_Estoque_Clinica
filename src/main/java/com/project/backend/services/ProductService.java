package com.project.backend.services;

import com.project.backend.dto.ProductDTO;
import com.project.backend.entities.Category;
import com.project.backend.entities.Product;
import com.project.backend.entities.Stock;
import com.project.backend.entities.Supplier;
import com.project.backend.repositories.CategoryRepository;
import com.project.backend.repositories.ProductRepository;
import com.project.backend.repositories.StockRepository;
import com.project.backend.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockService stockService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    public Product saveProduct(ProductDTO productDTO) {


        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDataCompra(productDTO.getDataCompra());
        product.setDataValidade(productDTO.getDataValidade());


        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));


        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));


        product.setSupplier(supplier);
        product.setCategory(category);


        Product savedProduct = productRepository.save(product);


        stockService.RegisterStockEntry(savedProduct);

        return savedProduct;
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
