package com.project.backend.web.controller;

import com.project.backend.entities.Categoria;
import com.project.backend.entities.Fornecedor;
import com.project.backend.repositories.CategoryRepository;
import com.project.backend.repositories.FornecedorRepository;
import com.project.backend.services.FornecedorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("suppliers")
public class FornecedorController {

    @Autowired
    private FornecedorServices fornecedorServices;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;


    @PostMapping("/register")
    public ResponseEntity<Fornecedor> cadastrarFornecedor(@RequestBody Fornecedor fornecedor) {

        String nomeCategoria = fornecedor.getCategoria().getName();


        Categoria categoriaExistente = categoryRepository.findByName(nomeCategoria)
                .orElseGet(() -> {
                    Categoria novaCategoria = new Categoria();
                    novaCategoria.setName(nomeCategoria);
                    return categoryRepository.save(novaCategoria);
                });


        fornecedor.setCategoria(categoriaExistente);


        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorSalvo);
    }



    @GetMapping
    public ResponseEntity<List<Fornecedor>> getAllFornecedores() {
        List<Fornecedor> fornecedores = fornecedorServices.getAllFornecedores();
        return ResponseEntity.ok(fornecedores);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Fornecedor> obterFornecedorPorId(@PathVariable Long id) {
        Optional<Fornecedor> fornecedor = fornecedorServices.getFornecedorById(id);
        return fornecedor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Fornecedor> updateFornecedor(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        Optional<Fornecedor> fornecedorExistente = fornecedorServices.getFornecedorById(id);
        if (fornecedorExistente.isPresent()) {
            fornecedor.setId(id);
            Fornecedor fornecedorAtualizado = fornecedorServices.updateFornecedor(fornecedor);
            return ResponseEntity.ok(fornecedorAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Long id) {
        Optional<Fornecedor> fornecedorExistente = fornecedorServices.getFornecedorById(id);
        if (fornecedorExistente.isPresent()) {
            fornecedorServices.deleteFornecedor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
