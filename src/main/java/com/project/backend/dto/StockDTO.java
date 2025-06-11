package com.project.backend.dto;

import com.project.backend.entities.Stock;
import lombok.Data;

import java.time.LocalDate;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockDTO {
    private Long id;
    private String productName;
    private String supplierName;
    private int quantity;
    private String tipoMovimento;
    private LocalDate dataMovimento;
    private int qtdComprada;

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.productName = stock.getProduct().getName();
        this.supplierName = stock.getSupplier().getSocialname();
        this.quantity = stock.getQuantity();
        this.tipoMovimento = stock.getTipoMovimento();
        this.dataMovimento = stock.getDataMovimento();
        this.qtdComprada = stock.getQtdComprada();
}
}
