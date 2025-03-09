package com.project.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private String tipoMovimento;

    @Column(nullable = false)
    private java.time.LocalDate dataMovimento;
}
