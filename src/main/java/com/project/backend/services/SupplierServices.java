package com.project.backend.services;

import com.project.backend.entities.Product;
import com.project.backend.entities.Supplier;
import com.project.backend.repositories.FornecedorRepository;
import com.project.backend.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServices {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProductRepository productRepository;

    public Supplier registerSupplier(Supplier supplier) {
        if (supplier.getProducts() != null) {
            for (Product product : supplier.getProducts()) {
                product.setSupplier(supplier);
            }
        }
        return fornecedorRepository.save(supplier);
    }

    public List<Supplier> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    public Optional<Supplier> getFornecedorById(Long id) {
        return fornecedorRepository.findById(id);
    }

    public Supplier updateFornecedor(Supplier supplier) {

        return fornecedorRepository.save(supplier);
    }

    public void deleteFornecedor(Long id) {
        fornecedorRepository.deleteById(id);
    }
}
