package com.project.backend.dto;

import com.project.backend.entities.Stock;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StockDTO {
    private Long id;
    private String productName;
    private int quantity;
    private String tipoMovimento;
    private LocalDate dataMovimento;

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.productName = stock.getProduct().getName();
        this.quantity = stock.getQuantity();
        this.tipoMovimento = stock.getTipoMovimento();
        this.dataMovimento = stock.getDataMovimento();
    }


}
