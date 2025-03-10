package com.project.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ must follow the pattern: XX.XXX.XXX/XXXX-XX")
    @Column(nullable = false, unique = true)
    private String cnpj;

    @Size(min = 2, message = "Name must be at least 2 characters long")
    @Column(nullable = false)
    private String razaosocial;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP must follow the pattern xxxxx-xxx")
    @Column(nullable = false)
    private String cep;

    @Size(min = 3, message = "Category name must be at least 3 characters long")
    @Column(nullable = false)
    private String categoria;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> products;
}
