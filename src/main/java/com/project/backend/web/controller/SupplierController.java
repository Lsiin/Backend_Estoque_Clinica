package com.project.backend.web.controller;

import com.project.backend.entities.Category;
import com.project.backend.entities.Supplier;
import com.project.backend.repositories.CategoryRepository;
import com.project.backend.repositories.FornecedorRepository;
import com.project.backend.services.SupplierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("suppliers")
public class SupplierController {

    @Autowired
    private SupplierServices supplierServices;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;


    @PostMapping("/register")
    public ResponseEntity<Supplier> cadastrarFornecedor(@RequestBody Supplier supplier) {

        String nomeCategoria = supplier.getCategory().getName();


        Category categoryExistente = categoryRepository.findByName(nomeCategoria)
                .orElseGet(() -> {
                    Category novaCategory = new Category();
                    novaCategory.setName(nomeCategoria);
                    return categoryRepository.save(novaCategory);
                });


        supplier.setCategory(categoryExistente);


        Supplier supplierSalvo = fornecedorRepository.save(supplier);

        return ResponseEntity.status(HttpStatus.CREATED).body(supplierSalvo);
    }



    @GetMapping
    public ResponseEntity<List<Supplier>> getAllFornecedores() {
        List<Supplier> fornecedores = supplierServices.getAllFornecedores();
        return ResponseEntity.ok(fornecedores);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Supplier> obterFornecedorPorId(@PathVariable Long id) {
        Optional<Supplier> fornecedor = supplierServices.getFornecedorById(id);
        return fornecedor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Supplier> updateFornecedor(@PathVariable Long id, @RequestBody Supplier supplier) {
        Optional<Supplier> fornecedorExistente = supplierServices.getFornecedorById(id);
        if (fornecedorExistente.isPresent()) {
            supplier.setId(id);
            Supplier supplierAtualizado = supplierServices.updateFornecedor(supplier);
            return ResponseEntity.ok(supplierAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Long id) {
        Optional<Supplier> fornecedorExistente = supplierServices.getFornecedorById(id);
        if (fornecedorExistente.isPresent()) {
            supplierServices.deleteFornecedor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
