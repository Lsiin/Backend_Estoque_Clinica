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
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

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


        return product;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setCategory(productDetails.getCategory());
            product.setSupplier(productDetails.getSupplier());
            return productRepository.save(product);
        });
    }


    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public void processProductSheet(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Pega a primeira aba da planilha
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignorar cabeçalho
    
                // Ler os dados da célula usando o método universal
                String nomeProduto = (String) getCellValue(row.getCell(3));
                Double precoAsDouble = (Double) getCellValue(row.getCell(4));
                Float preco = precoAsDouble != null ? precoAsDouble.floatValue() : null; // Converter para Float
                Double quantidadeAsDouble = (Double) getCellValue(row.getCell(5));
                Integer quantidade = quantidadeAsDouble != null ? quantidadeAsDouble.intValue() : null; // Converter para Integer
                Double categoriaAsDouble = (Double) getCellValue(row.getCell(6));
                Long categoriaId = categoriaAsDouble != null ? categoriaAsDouble.longValue() : null; // Converter para Long
                Double fornecedorAsDouble = (Double) getCellValue(row.getCell(7));
                Long fornecedorId = fornecedorAsDouble != null ? fornecedorAsDouble.longValue() : null; // Converter para Long
                LocalDate dataCompra = (LocalDate) getCellValue(row.getCell(1));
                LocalDate dataValidade = (LocalDate) getCellValue(row.getCell(2));
    
                // Criar e preencher ProductDTO
                ProductDTO productDTO = new ProductDTO();
                productDTO.setName(nomeProduto);
                productDTO.setPrice(preco);
                productDTO.setQuantity(quantidade);
                productDTO.setDataCompra(dataCompra);
                productDTO.setDataValidade(dataValidade);
                productDTO.setCategoryId(categoriaId);
                productDTO.setSupplierId(fornecedorId);
    
                // Salvar o produto usando o método já existente
                saveProduct(productDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar a planilha: " + e.getMessage(), e);
        }
    }
    
    
    
    
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null; // Retorna null se a célula estiver vazia
        }
        switch (cell.getCellType()) {
            case STRING:
                String stringValue = cell.getStringCellValue().trim();
                // Tenta converter o texto para número, se for um número válido
                if (stringValue.matches("-?\\d+(\\.\\d+)?")) { // Verifica se é um número
                    return Double.parseDouble(stringValue); // Converte para Double
                }
                return stringValue; // Retorna como texto se não for número
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate(); // Retorna a data como LocalDate
                } else {
                    return cell.getNumericCellValue(); // Retorna o número
                }
            case BOOLEAN:
                return cell.getBooleanCellValue(); // Retorna valor booleano
            case BLANK:
                return null; // Retorna null para células em branco
            default:
                throw new IllegalArgumentException("Tipo de célula não suportado: " + cell.getCellType());
        }
    }
    

}
