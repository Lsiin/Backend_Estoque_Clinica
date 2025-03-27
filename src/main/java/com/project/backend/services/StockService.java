package com.project.backend.services;

import com.project.backend.entities.Product;
import com.project.backend.entities.Stock;
import com.project.backend.repositories.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void RegisterStockEntry(Product product) {
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setSupplier(product.getSupplier());
        stock.setQuantity(product.getQuantity());
        stock.setTipoMovimento("Entrada");
        stock.setDataMovimento(LocalDate.now());
        stock.setQtdComprada(product.getQuantity());

        stockRepository.save(stock);
    }
}