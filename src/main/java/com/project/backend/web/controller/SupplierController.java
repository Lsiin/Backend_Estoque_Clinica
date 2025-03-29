package com.project.backend.web.controller;

import com.project.backend.entities.Supplier;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.SupplierRepository;
import com.project.backend.services.SupplierServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Suppliers", description = "Supplier management endpoints")
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierServices supplierServices;

    @Operation(summary = "Register a new supplier",
            description = "Creates a new supplier with validation for required fields and CNPJ format",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - Supplier with this CNPJ already exists",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Invalid CNPJ format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<Supplier> cadastrarFornecedor(@RequestBody Supplier supplier) {
        Supplier savedSupplier = supplierServices.registerSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }

    @Operation(summary = "Get all suppliers",
            description = "Retrieves a list of all registered suppliers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of suppliers retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Supplier.class, type = "array"))),
                    @ApiResponse(responseCode = "404", description = "No suppliers found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllFornecedores() {
        List<Supplier> suppliers = supplierServices.getAllFornecedores();
        if(suppliers.isEmpty()) {
            throw new GlobalExceptionHandler.UserNotFoundException("Suppliers not found");
        }
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Get supplier by ID",
            description = "Retrieves a specific supplier by their unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
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
            description = "Updates an existing supplier's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Supplier.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity - Invalid CNPJ format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @PutMapping("/update/{id}")
    public ResponseEntity<Supplier> updateFornecedor(@PathVariable Long id, @RequestBody Supplier supplier) {
        Optional<Supplier> existSupplier = supplierServices.getFornecedorById(id);
        if (existSupplier.isPresent()) {
            Supplier supplierAtualizado = supplierServices.updateFornecedor(supplier);
            return ResponseEntity.ok(supplierAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a supplier",
            description = "Deletes a supplier by their unique identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
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


}