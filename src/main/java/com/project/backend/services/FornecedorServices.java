package com.project.backend.services;

import com.project.backend.entities.Fornecedor;
import com.project.backend.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorServices {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public Fornecedor addFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> getFornecedorById(Long id) {
        return fornecedorRepository.findById(id);
    }

    public Fornecedor updateFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public void deleteFornecedor(Long id) {
        fornecedorRepository.deleteById(id);
    }
}
