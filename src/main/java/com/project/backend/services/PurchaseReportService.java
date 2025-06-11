package com.project.backend.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.backend.entities.PurchaseItem;
import com.project.backend.entities.PurchaseOrder;
import com.project.backend.repositories.PurchaseOrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PurchaseReportService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generatePdfReport(Long purchaseId) throws DocumentException, IOException {
        PurchaseOrder purchase = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Purchase Order Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // spacing

            document.add(new Paragraph("Purchase Order ID: " + purchase.getId()));
            document.add(new Paragraph("Supplier: " + purchase.getSupplier().getSocialname()));
            document.add(new Paragraph("Order Date: " + purchase.getOrderDate().format(DATE_FORMATTER)));
            document.add(new Paragraph("Expected Delivery: " + purchase.getExpectedDeliveryDate().format(DATE_FORMATTER)));
            document.add(new Paragraph("Status: " + purchase.getStatus()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            Stream.of("Product", "Quantity", "Unit Price", "Total").forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });

            double total = 0;
            for (PurchaseItem item : purchase.getItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("$%.2f", item.getUnitPrice()));
                double itemTotal = item.getQuantity() * item.getUnitPrice();
                table.addCell(String.format("$%.2f", itemTotal));
                total += itemTotal;
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Amount: $" + String.format("%.2f", total)));

            document.close();
            return out.toByteArray();
        }
    }

    public byte[] generateExcelReport(Long purchaseId) throws IOException {
        PurchaseOrder purchase = purchaseOrderRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Purchase Order Report");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Purchase Order Report");
            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Purchase details
            sheet.createRow(2).createCell(0).setCellValue("Purchase Order ID:");
            sheet.getRow(2).createCell(1).setCellValue(purchase.getId());

            sheet.createRow(3).createCell(0).setCellValue("Supplier:");
            sheet.getRow(3).createCell(1).setCellValue(purchase.getSupplier().getSocialname());

            sheet.createRow(4).createCell(0).setCellValue("Order Date:");
            sheet.getRow(4).createCell(1).setCellValue(purchase.getOrderDate().format(DATE_FORMATTER));

            sheet.createRow(5).createCell(0).setCellValue("Expected Delivery:");
            sheet.getRow(5).createCell(1).setCellValue(purchase.getExpectedDeliveryDate().format(DATE_FORMATTER));

            sheet.createRow(6).createCell(0).setCellValue("Status:");
            sheet.getRow(6).createCell(1).setCellValue(purchase.getStatus());

            // Header for items
            Row headerRow = sheet.createRow(8);
            String[] headers = {"Product", "Quantity", "Unit Price", "Total"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Add items
            int rowNum = 9;
            double total = 0;
            for (PurchaseItem item : purchase.getItems()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getProduct().getName());
                row.createCell(1).setCellValue(item.getQuantity());
                row.createCell(2).setCellValue(item.getUnitPrice());
                double itemTotal = item.getQuantity() * item.getUnitPrice();
                row.createCell(3).setCellValue(itemTotal);
                total += itemTotal;
            }

            // Add total
            Row totalRow = sheet.createRow(rowNum + 1);
            totalRow.createCell(0).setCellValue("Total Amount:");
            totalRow.createCell(1).setCellValue(total);

            // Autosize columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
