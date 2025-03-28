package com.project.backend.web.controller;

import com.project.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> generateStockReport() throws IOException {
        byte[] reportContent = reportService.generateStockReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(reportContent);
    }
}