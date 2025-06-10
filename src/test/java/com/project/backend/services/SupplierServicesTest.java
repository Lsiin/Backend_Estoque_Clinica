package com.project.backend.services;

import com.project.backend.entities.Supplier;
import com.project.backend.repositories.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServicesTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServices supplierServices;

    private Supplier sampleSupplier;
    private Supplier sampleSupplier2;

    @BeforeEach
    void setUp() {
        sampleSupplier = new Supplier(1L, "Fornecedor A", "12345678901234", "contato@a.com", "11999999999");
        sampleSupplier2 = new Supplier(2L, "Fornecedor B", "98765432109876", "contato@b.com", "11888888888");
    }

    @Test
    void registerSupplier_ShouldSaveAndReturnSupplier() {
        // Arrange
        when(supplierRepository.save(any(Supplier.class))).thenReturn(sampleSupplier);

        // Act
        Supplier result = supplierServices.registerSupplier(sampleSupplier);

        // Assert
        assertNotNull(result);
        assertEquals(sampleSupplier.getId(), result.getId());
        assertEquals(sampleSupplier.getSocialname(), result.getSocialname());
        verify(supplierRepository, times(1)).save(sampleSupplier);
    }

    @Test
    void getAllFornecedores_ShouldReturnAllSuppliers() {
        // Arrange
        List<Supplier> suppliers = Arrays.asList(sampleSupplier, sampleSupplier2);
        when(supplierRepository.findAll()).thenReturn(suppliers);

        // Act
        List<Supplier> result = supplierServices.getAllFornecedores();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void getAllFornecedores_ShouldReturnEmptyListWhenNoSuppliers() {
        // Arrange
        when(supplierRepository.findAll()).thenReturn(List.of());

        // Act
        List<Supplier> result = supplierServices.getAllFornecedores();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void getFornecedorById_ShouldReturnSupplierWhenExists() {
        // Arrange
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(sampleSupplier));

        // Act
        Optional<Supplier> result = supplierServices.getFornecedorById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleSupplier.getId(), result.get().getId());
        verify(supplierRepository, times(1)).findById(1L);
    }

    @Test
    void getFornecedorById_ShouldReturnEmptyWhenNotExists() {
        // Arrange
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Supplier> result = supplierServices.getFornecedorById(99L);

        // Assert
        assertTrue(result.isEmpty());
        verify(supplierRepository, times(1)).findById(99L);
    }

    @Test
    void updateFornecedor_ShouldUpdateAndReturnSupplier() {
        // Arrange
        Supplier updatedSupplier = new Supplier(1L, "Fornecedor A Atualizado", "12345678901234", "novo@a.com", "11999999999");
        when(supplierRepository.save(any(Supplier.class))).thenReturn(updatedSupplier);

        // Act
        Supplier result = supplierServices.updateFornecedor(updatedSupplier);

        // Assert
        assertNotNull(result);
        assertEquals("Fornecedor A Atualizado", result.getSocialname());
        verify(supplierRepository, times(1)).save(updatedSupplier);
    }

    @Test
    void deleteFornecedor_ShouldCallDeleteMethod() {
        // Arrange - não precisa de when() pois o método é void
        doNothing().when(supplierRepository).deleteById(1L);

        // Act
        supplierServices.deleteFornecedor(1L);

        // Assert
        verify(supplierRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFornecedor_ShouldHandleNonExistentId() {
        // Arrange
        doNothing().when(supplierRepository).deleteById(99L);

        // Act & Assert
        assertDoesNotThrow(() -> supplierServices.deleteFornecedor(99L));
        verify(supplierRepository, times(1)).deleteById(99L);
    }
}