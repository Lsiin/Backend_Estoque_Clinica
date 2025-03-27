package com.project.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = " STOCK|PURCHASE|EXPIRATION")
    @Column(nullable = false)
    private String reportType;

    @Column(nullable = false)
    private LocalDateTime generationDate;

    @Pattern(regexp = " PDF|EXCEL|CSV")
    @Column(nullable = false)
    private String format;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User generatedBy;
}