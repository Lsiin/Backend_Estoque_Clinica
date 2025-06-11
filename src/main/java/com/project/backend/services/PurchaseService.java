package com.project.backend.services;

import com.itextpdf.text.DocumentException;
import com.project.backend.dto.PurchaseOrderDTO;
import com.project.backend.entities.*;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderDTO orderDTO) {
        PurchaseOrder order = new PurchaseOrder();

        Supplier supplier = supplierRepository.findById(orderDTO.getSupplierId())
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Supplier not found"));

        order.setSupplier(supplier);
        order.setOrderDate(LocalDate.now());
        order.setExpectedDeliveryDate(orderDTO.getExpectedDeliveryDate());
        order.setStatus("PENDING");

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        List<PurchaseItem> items = orderDTO.getItems().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Product not found"));

            PurchaseItem item = new PurchaseItem();
            item.setPurchaseOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());

            return purchaseItemRepository.save(item);
        }).collect(Collectors.toList());

        savedOrder.setItems(items);
        return savedOrder;
    }

    @Transactional
    public PurchaseOrder completePurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Order not found"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Only PENDING orders can be completed");
        }

        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);


            Stock stockEntry = new Stock();
            stockEntry.setProduct(product);
            stockEntry.setSupplier(order.getSupplier());
            stockEntry.setQuantity(item.getQuantity());
            stockEntry.setTipoMovimento("ENTRADA");
            stockEntry.setDataMovimento(LocalDate.now());
            stockEntry.setQtdComprada(item.getQuantity());
            stockRepository.save(stockEntry);
        });

        order.setStatus("COMPLETED");
        return purchaseOrderRepository.save(order);
    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void suggestPurchases() {
        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getQuantity() < 10)
                .collect(Collectors.toList());

        List<Product> expiringSoonProducts = productRepository.findAll().stream()
                .filter(p -> p.getDataValidade() != null &&
                        p.getDataValidade().isBefore(LocalDate.now().plusDays(30)))
                .collect(Collectors.toList());

    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    @Transactional
    public void deletePurchase(Long id) {
        purchaseOrderRepository.deleteById(id);
    }

}