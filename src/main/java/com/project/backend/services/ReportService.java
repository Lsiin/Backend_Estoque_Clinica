package com.project.backend.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.backend.entities.Product;
import com.project.backend.repositories.ProductRepository;
import com.project.backend.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProductRepository productRepository;


    public byte[] generateStockReport() throws IOException {
        List<Product> products = productRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Estoque");


            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nome", "Fornecedor","Categoria", "Quantidade", "Preço", "Data_compra", "Data_vencimento"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getSupplier().getSocialname());
                row.createCell(3).setCellValue(product.getCategory().getNameCategory());
                row.createCell(4).setCellValue(product.getQuantity());
                row.createCell(5).setCellValue(product.getPrice());
                row.createCell(6).setCellValue(product.getDataCompra().format(formatter));
                row.createCell(7).setCellValue(product.getDataValidade().format(formatter));

            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
    public byte[] generateStockReportPdf() throws DocumentException {
        List<Product> products = productRepository.findAll();

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font font = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD);
        Paragraph paragraph = new Paragraph("Relatório de Estoque", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidths(new int[]{1, 3, 3, 2, 2, 2, 3, 3});

        Stream.of("ID", "Nome", "Fornecedor",  "Categoria", "Quantidade", "Preço", "Data_compra", "Data_vencimento")
                .forEach(headerTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(headerTitle));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(header);
                });

        for (Product product : products) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getSupplier().getSocialname());
            table.addCell(product.getCategory().getNameCategory());
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.format("%.2f", product.getPrice()));
            table.addCell(product.getDataCompra().toString());
            table.addCell(product.getDataValidade().toString());
        }

        document.add(table);
        document.close();
        return outputStream.toByteArray();
    }
}

