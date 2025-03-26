package com.project.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;


    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonBackReference
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @NotNull(message = "The price cannot be null")
    @Column(nullable = false)
    private Float price;

    @NotNull
    @Column(nullable = false)
    private Integer quantity;
}
