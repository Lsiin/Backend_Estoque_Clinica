package com.project.backend.services;

import com.project.backend.entities.Supplier;
import com.project.backend.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServices {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Supplier addFornecedor(Supplier supplier) {
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
