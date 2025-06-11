package com.project.backend.services;

import com.project.backend.entities.Product;
import com.project.backend.entities.Stock;
import com.project.backend.entities.Supplier;
import com.project.backend.repositories.StockRepository;
import com.project.backend.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Product product;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setSocialname("Test Supplier");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setSupplier(supplier);
        product.setQuantity(10);
    }

    @Test
    void testRegisterStockEntry() {
        when(stockRepository.save(any(Stock.class))).thenReturn(new Stock());

        stockService.RegisterStockEntry(product);

        verify(stockRepository, times(1)).save(any(Stock.class));

        // Verify the stock object properties
        verify(stockRepository).save(argThat(stock ->
                stock.getProduct().equals(product) &&
                        stock.getSupplier().equals(supplier) &&
                        stock.getQuantity() == 10 &&
                        stock.getTipoMovimento().equals("Entrada") &&
                        stock.getDataMovimento().equals(LocalDate.now()) &&
                        stock.getQtdComprada() == 10
        ));
    }

    @Test
    void testRegisterStockEntryWithNullProduct() {
        assertThrows(NullPointerException.class, () -> stockService.RegisterStockEntry(null));
        verify(stockRepository, never()).save(any(Stock.class));
    }
}