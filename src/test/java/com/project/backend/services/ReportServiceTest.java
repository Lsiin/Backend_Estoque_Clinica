package com.project.backend.services;

import com.itextpdf.text.DocumentException;
import com.project.backend.entities.Category;
import com.project.backend.entities.Product;
import com.project.backend.entities.Supplier;
import com.project.backend.repositories.ProductRepository;
import com.project.backend.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReportService reportService;

    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setSocialname("Test Supplier");

        Category category = new Category();
        category.setId(1L);
        category.setNameCategory("Test Category");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setSupplier(supplier);
        product1.setCategory(category);
        product1.setQuantity(10);
        product1.setPrice(99.99F);
        product1.setDataCompra(LocalDate.of(2025, 1, 1));
        product1.setDataValidade(LocalDate.of(2025, 12, 31));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setSupplier(supplier);
        product2.setCategory(category);
        product2.setQuantity(20);
        product2.setPrice(49.99F);
        product2.setDataCompra(LocalDate.of(2025, 2, 1));
        product2.setDataValidade(LocalDate.of(2025, 11, 30));

        mockProducts = Arrays.asList(product1, product2);
    }

    @Test
    void testGenerateStockReport() throws IOException {
        when(productRepository.findAll()).thenReturn(mockProducts);

        byte[] result = reportService.generateStockReport();

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGenerateStockReportEmptyList() throws IOException {
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        byte[] result = reportService.generateStockReport();

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGenerateStockReportPdf() throws DocumentException {
        when(productRepository.findAll()).thenReturn(mockProducts);

        byte[] result = reportService.generateStockReportPdf();

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGenerateStockReportPdfEmptyList() throws DocumentException {
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        byte[] result = reportService.generateStockReportPdf();

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGenerateStockReportPdfThrowsDocumentException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(DocumentException.class, () -> reportService.generateStockReportPdf());
        verify(productRepository, times(1)).findAll();
    }
}