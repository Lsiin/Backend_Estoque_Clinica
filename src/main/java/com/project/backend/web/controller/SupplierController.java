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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Suppliers", description = "Supplier management endpoints")
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierServices supplierServices;

    @Autowired
    private SupplierRepository supplierRepository;

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
        if (supplier.getSocialname() == null || supplier.getSocialname().isEmpty()) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Social name cannot be null");
        }
        if (supplier.getCategory() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Category cannot be null");
        }
        if (supplier.getCnpj() == null || supplier.getCnpj().isEmpty()) {
            throw new GlobalExceptionHandler.ResourceBeNullException("CNPJ cannot be null");
        }
        if (supplier.getCep() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("CEP cannot be null");
        }

        if (!isValidCnpj(supplier.getCnpj())) {
            throw new GlobalExceptionHandler.InvalidCnpjFormatException("CNPJ is invalid");
        }

        Optional<Supplier> existingSupplier = supplierRepository.findByCnpj(supplier.getCnpj());
        if (existingSupplier.isPresent()) {
           throw new GlobalExceptionHandler.DuplicateDataException("A supplier with this CNPJ already exists.");
        }

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
            throw new GlobalExceptionHandler.SupplierNotFoundException("Suppliers not found");
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
    public ResponseEntity<Object> updateFornecedor(@PathVariable Long id, @RequestBody Supplier supplierDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                errors.put(fieldName, error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Supplier updatedSupplier = supplierRepository.findById(id)
                    .map(existingSupplier -> {
                        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                                .getAuthorities().stream()
                                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));

                        boolean isSelf = existingSupplier.getCnpj().equals(
                                SecurityContextHolder.getContext().getAuthentication().getName());
                        if (!isAdmin && !isSelf) {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update suppliers with admin role");
                        }
                        if (supplierDetails.getSocialname() == null || supplierDetails.getSocialname().isEmpty()) {
                            throw new GlobalExceptionHandler.ResourceBeNullException("Social name cannot be null");
                        }
                        if (supplierDetails.getCategory() == null) {
                            throw new GlobalExceptionHandler.ResourceBeNullException("Category cannot be null");
                        }
                        if (supplierDetails.getCnpj() == null || supplierDetails.getCnpj().isEmpty()) {
                            throw new GlobalExceptionHandler.ResourceBeNullException("CNPJ cannot be null");
                        }
                        if (supplierDetails.getCep() == null) {
                            throw new GlobalExceptionHandler.ResourceBeNullException("CEP cannot be null");
                        }
                        if (!isValidCnpj(supplierDetails.getCnpj())) {
                            throw new GlobalExceptionHandler.InvalidCnpjFormatException("CNPJ is invalid");
                        }
                        if (!supplierDetails.getCep().matches("\\d{5}-\\d{3}")) {
                            throw new GlobalExceptionHandler.InvalidCepFormatException("CEP must follow the pattern xxxxx-xxx");
                        }

                        existingSupplier.setSocialname(supplierDetails.getSocialname());
                        existingSupplier.setCategory(supplierDetails.getCategory());
                        existingSupplier.setCnpj(supplierDetails.getCnpj());
                        existingSupplier.setCep(supplierDetails.getCep());

                        return supplierRepository.save(existingSupplier);
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

            return ResponseEntity.ok(updatedSupplier);
        } catch (GlobalExceptionHandler.ResourceBeNullException |
                 GlobalExceptionHandler.InvalidCnpjFormatException |
                 GlobalExceptionHandler.InvalidCepFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
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
            throw new GlobalExceptionHandler.SupplierNotFoundException("Supplier not found with ID:" + id);
        }
    }

    private boolean isValidCnpj(String cnpj) {
        return cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }
}