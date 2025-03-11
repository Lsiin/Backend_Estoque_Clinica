package com.project.backend.web.controller;

import com.project.backend.entities.Supplier;
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





    @PostMapping("/register")
    public ResponseEntity<Supplier> cadastrarFornecedor(@RequestBody Supplier supplier) {
        Supplier savedSupplier = supplierServices.registerSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }




    @GetMapping
    public ResponseEntity<List<Supplier>> getAllFornecedores() {
        List<Supplier> suppliers = supplierServices.getAllFornecedores();
        return ResponseEntity.ok(suppliers);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Supplier> obterFornecedorPorId(@PathVariable Long id) {
            Optional<Supplier> supplier = supplierServices.getFornecedorById(id);
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Supplier> updateFornecedor(@PathVariable Long id, @RequestBody Supplier supplier) {
        Optional<Supplier> existSupplier = supplierServices.getFornecedorById(id);
        if (existSupplier.isPresent()) {
            supplier.setId(id);
            Supplier supplierAtualizado = supplierServices.updateFornecedor(supplier);
            return ResponseEntity.ok(supplierAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Long id) {
        Optional<Supplier> existSupplier = supplierServices.getFornecedorById(id);
        if (existSupplier.isPresent()) {
            supplierServices.deleteFornecedor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
