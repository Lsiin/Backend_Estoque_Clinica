package com.project.backend.web.controller;

import com.project.backend.entities.Supplier;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.SupplierRepository;
import com.project.backend.services.SupplierServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierServices supplierServices;
    @Autowired
    private SupplierRepository supplierRepository;

    @Operation(summary = "Register a supplier",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier registration successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "400", description = "Could not create supplier",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal error",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class))),
            })
    @PostMapping("/register")
    public ResponseEntity<Supplier> cadastrarFornecedor(@RequestBody Supplier supplier) {
        if (supplier.getCnpj() == null) {
            throw new GlobalExceptionHandler.InvalidCnpjFormatException("CNPJ cannot be null");
        }

        if (supplier.getSocialname() == null) {
            throw new GlobalExceptionHandler.DuplicateDataException("Social Name cannot be null");
        }

        if (supplier.getCep() == null) {
            throw new GlobalExceptionHandler.InvalidCepFormatException("CEP cannot be null");
        }

        if (supplier.getCategory() == null) {
            throw new GlobalExceptionHandler.DuplicateDataException("Category cannot be null");
        }

        if (!isValidCnpj(supplier.getCnpj())) {
            throw new GlobalExceptionHandler.InvalidCnpjFormatException("CNPJ invalid");
        }

        Optional<Supplier> existSupplierByCnpj = supplierRepository.findByCnpj(supplier.getCnpj());
        if (existSupplierByCnpj.isPresent()) {
            throw new GlobalExceptionHandler.DuplicateDataException("A supplier with this CNPJ already exists.");
        }

        Supplier savedSupplier = supplierServices.registerSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }

    @Operation(summary = "Get all suppliers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all suppliers",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "404", description = "No suppliers found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllFornecedores() {
        List<Supplier> suppliers = supplierServices.getAllFornecedores();
        if(suppliers.isEmpty()) {
            throw new GlobalExceptionHandler.UserNotFoundException("Suppliers not found");
        }
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Find supplier by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping("/get/{id}")
    public ResponseEntity<Supplier> obterFornecedorPorId(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierServices.getFornecedorById(id);
        if (supplier.isPresent()) {
            return ResponseEntity.ok(supplier.get());
        } else {
            throw new GlobalExceptionHandler.SupplierNotFoundException("Supplier not found with ID:" + id);
        }
    }

    @Operation(summary = "Update a supplier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class)))
            })
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

    @Operation(summary = "Delete a supplier",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier deleted successfully",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/Json", schema = @Schema(implementation = ErrorResponses.class)))
            })
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

    private boolean isValidCnpj(String cnpj) {
        return cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }
}
