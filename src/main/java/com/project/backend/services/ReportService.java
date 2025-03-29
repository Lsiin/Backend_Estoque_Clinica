package com.project.backend.services;

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
}