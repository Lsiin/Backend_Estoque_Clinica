package com.project.backend.dto;

import com.project.backend.entities.Product;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Long supplierId;
    private Long categoryId;
    private Float price;
    private Integer quantity;
    private LocalDate dataCompra;
    private LocalDate dataValidade;

}
