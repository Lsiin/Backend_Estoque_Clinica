package com.project.backend.services;

import com.project.backend.entities.Supplier;
import com.project.backend.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServices {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier registerSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllFornecedores() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getFornecedorById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier updateFornecedor(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void deleteFornecedor(Long id) {
        supplierRepository.deleteById(id);
    }
}