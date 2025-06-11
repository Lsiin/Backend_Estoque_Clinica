package com.project.backend.web.controller;

import com.itextpdf.text.DocumentException;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Reports", description = "Report generation endpoints")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Generate stock report",
            description = "Generates an Excel report with current stock information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report generated successfully",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Error generating report",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping("/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> generateStockReport() throws IOException {
      try {
          byte[] reportContent = reportService.generateStockReport();
          return ResponseEntity.ok()
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report.xlsx")
                  .contentType(MediaType.APPLICATION_OCTET_STREAM)
                  .body(reportContent);
      } catch (GlobalExceptionHandler.ResourceNotFoundException ex) {
          return ResponseEntity.notFound().build();
      }


    }
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdfReport() throws DocumentException {
        byte[] report = reportService.generateStockReportPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estoque.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(report);
    }

}