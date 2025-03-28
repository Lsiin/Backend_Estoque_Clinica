package com.project.backend.entities;

import jakarta.persistence.*;
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

    public enum ReportType {
        STOCK, PURCHASE, EXPIRATION
    }

    public enum Format {
        PDF, EXCEL, CSV
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Column(nullable = false)
    private LocalDateTime generationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Format format;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] content;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User generatedBy;
}