package com.project.backend.web.controller;

import com.project.backend.dto.PurchaseOrderDTO;
import com.project.backend.entities.PurchaseOrder;
import com.project.backend.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "purchase", description = "Purchase order management")
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Operation(summary = "Create a new purchase order")
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderDTO orderDTO) {
        PurchaseOrder order = purchaseService.createPurchaseOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Complete a purchase order")
    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> completePurchaseOrder(@PathVariable Long id) {
        PurchaseOrder order = purchaseService.completePurchaseOrder(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get all purchase orders")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseService.getAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get purchase order by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        Optional<PurchaseOrder> order = purchaseService.getPurchaseOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}