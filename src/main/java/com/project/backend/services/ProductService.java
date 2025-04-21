package com.project.backend.services;

import com.project.backend.dto.ProductDTO;
import com.project.backend.entities.Category;
import com.project.backend.entities.Product;
import com.project.backend.entities.Stock;
import com.project.backend.entities.Supplier;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.CategoryRepository;
import com.project.backend.repositories.ProductRepository;
import com.project.backend.repositories.StockRepository;
import com.project.backend.repositories.SupplierRepository;
import com.project.backend.web.controller.ErrorResponses;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public Product saveProduct(ProductDTO productDTO) {


        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDataCompra(productDTO.getDataCompra());
        product.setDataValidade(productDTO.getDataValidade());

        if (productDTO.getName() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Name cannot be null");
        }
        if (productDTO.getPrice() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Price cannot be null");
        }
        if (productDTO.getQuantity() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Quantity cannot be null");
        }
        if (productDTO.getDataCompra() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("DataCompra cannot be null");
        }
        if (productDTO.getDataValidade() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("DataValidade cannot be null");
        }

        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));


        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));


        product.setSupplier(supplier);
        product.setCategory(category);


        Product savedProduct = productRepository.save(product);


        stockService.RegisterStockEntry(savedProduct);

        return savedProduct;
    }



    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Product not found"));

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDataCompra(productDTO.getDataCompra());
        product.setDataValidade(productDTO.getDataValidade());

        if (productDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Supplier not found"));
            product.setSupplier(supplier);
        }
        if (productDTO.getName() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Name cannot be null");
        }
        if (productDTO.getPrice() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Price cannot be null");
        }
        if (productDTO.getQuantity() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Quantity cannot be null");
        }
        if (productDTO.getSupplierId() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Supplier ID cannot be null");
        }
        if (productDTO.getCategoryId() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("Category ID cannot be null");
        }
        if (productDTO.getDataCompra() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("DataCompra cannot be null");
        }
        if (productDTO.getDataValidade() == null) {
            throw new GlobalExceptionHandler.ResourceBeNullException("DataValidade cannot be null");
        }

        return product;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }





    @Transactional
    public boolean deleteProduct(Long id) {
        try {
            stockRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    public void processProductSheet(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
    

                String nomeProduto = (String) getCellValue(row.getCell(3));
                Double precoAsDouble = (Double) getCellValue(row.getCell(4));
                Float preco = precoAsDouble != null ? precoAsDouble.floatValue() : null;
                Double quantidadeAsDouble = (Double) getCellValue(row.getCell(5));
                Integer quantidade = quantidadeAsDouble != null ? quantidadeAsDouble.intValue() : null;
                Double categoriaAsDouble = (Double) getCellValue(row.getCell(6));
                Long categoriaId = categoriaAsDouble != null ? categoriaAsDouble.longValue() : null;
                Double fornecedorAsDouble = (Double) getCellValue(row.getCell(7));
                Long fornecedorId = fornecedorAsDouble != null ? fornecedorAsDouble.longValue() : null;
                LocalDate dataCompra = (LocalDate) getCellValue(row.getCell(1));
                LocalDate dataValidade = (LocalDate) getCellValue(row.getCell(2));
    

                ProductDTO productDTO = new ProductDTO();
                productDTO.setName(nomeProduto);
                productDTO.setPrice(preco);
                productDTO.setQuantity(quantidade);
                productDTO.setDataCompra(dataCompra);
                productDTO.setDataValidade(dataValidade);
                productDTO.setCategoryId(categoriaId);
                productDTO.setSupplierId(fornecedorId);
    

                saveProduct(productDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar a planilha: " + e.getMessage(), e);
        }
    }
    
    
    
    
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                String stringValue = cell.getStringCellValue().trim();

                if (stringValue.matches("-?\\d+(\\.\\d+)?")) {
                    return Double.parseDouble(stringValue);
                }
                return stringValue;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case BLANK:
                return null;
            default:
                throw new IllegalArgumentException("Tipo de célula não suportado: " + cell.getCellType());
        }
    }
    

}
