package com.project.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category category;

    @NotNull
    @NumberFormat(pattern = "R$ #,##0.##")
    @Column(nullable = false)
    private float preco;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Stock> qtdestoque;
}
