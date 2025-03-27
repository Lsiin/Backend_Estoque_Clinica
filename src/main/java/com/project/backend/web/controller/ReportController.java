package com.project.backend.web.controller;

import com.project.backend.entities.Report;
import com.project.backend.entities.User;
import com.project.backend.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "reports", description = "Report generation and management")
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Generate stock report")
    @GetMapping("/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> generateStockReport(@AuthenticationPrincipal User user) throws IOException {
        byte[] reportContent = reportService.generateStockReport(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(reportContent);
    }

    @Operation(summary = "Generate expiration report")
    @GetMapping("/expiration")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> generateExpirationReport(@AuthenticationPrincipal User user) throws IOException {
        byte[] reportContent = reportService.generateExpirationReport(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expiration_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(reportContent);
    }

    @Operation(summary = "Get all generated reports")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @Operation(summary = "Get report by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}