package com.project.backend.dto;


import lombok.*;

@Data
public class ProductDTO {
    private String name;
    private Float price;
    private Integer quantity;
    private Long supplierId;
    private Long categoryId;


}
