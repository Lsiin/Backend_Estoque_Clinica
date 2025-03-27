package com.project.backend.services;

import com.project.backend.entities.Product;
import com.project.backend.entities.Report;
import com.project.backend.entities.User;
import com.project.backend.repositories.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    public byte[] generateStockReport(User user) throws IOException {
        List<Product> products = productRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Report");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Product Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Expiration Date");

            // Dados
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getQuantity());
                row.createCell(3).setCellValue(product.getPrice());
                if (product.getDataValidade() != null) {
                    row.createCell(4).setCellValue(product.getDataValidade().toString());
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            Report report = new Report();
            report.setReportType("STOCK");
            report.setGenerationDate(LocalDateTime.now());
            report.setFormat("EXCEL");
            report.setContent(outputStream.toByteArray());
            report.setGeneratedBy(user);
            reportRepository.save(report);

            return outputStream.toByteArray();
        }
    }

    public byte[] generateExpirationReport(User user) throws IOException {
        List<Product> expiringProducts = productRepository.findAll().stream()
                .filter(p -> p.getDataValidade() != null &&
                        p.getDataValidade().isBefore(LocalDate.now().plusDays(60)))
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expiration Report");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Product Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Expiration Date");
            headerRow.createCell(4).setCellValue("Days Until Expiration");

            // Dados
            int rowNum = 1;
            for (Product product : expiringProducts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getQuantity());
                row.createCell(3).setCellValue(product.getDataValidade().toString());
                row.createCell(4).setCellValue(LocalDate.now().until(product.getDataValidade()).getDays());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // Salvar o relatório no banco de dados
            Report report = new Report();
            report.setReportType("EXPIRATION");
            report.setGenerationDate(LocalDateTime.now());
            report.setFormat("EXCEL");
            report.setContent(outputStream.toByteArray());
            report.setGeneratedBy(user);
            reportRepository.save(report);

            return outputStream.toByteArray();
        }
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }
}