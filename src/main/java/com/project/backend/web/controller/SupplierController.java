package com.project.backend.web.controller;

import com.project.backend.entities.Supplier;
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
@RequestMapping("suppliers")
public class SupplierController {

    @Autowired
    private SupplierServices supplierServices;




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
        Supplier savedSupplier = supplierServices.registerSupplier(supplier);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
    }




    @GetMapping
    public ResponseEntity<List<Supplier>> getAllFornecedores() {
        List<Supplier> suppliers = supplierServices.getAllFornecedores();
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
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
                    @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
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
}
