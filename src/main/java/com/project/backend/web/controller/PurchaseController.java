package com.project.backend.web.controller;

import com.project.backend.dto.PurchaseOrderDTO;
import com.project.backend.entities.PurchaseOrder;
import com.project.backend.exceptions.GlobalExceptionHandler;
import com.project.backend.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Purchase", description = "Purchase order management")
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Operation(summary = "Create a new purchase order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Purchase order created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseOrderDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrderDTO orderDTO) {
        PurchaseOrder order = purchaseService.createPurchaseOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Complete a purchase order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Purchase order completed successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseOrderDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "Purchase order not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @PutMapping("/complete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> completePurchaseOrder(@PathVariable Long id) {
        PurchaseOrder order = purchaseService.completePurchaseOrder(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get all purchase orders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseOrderDTO.class, type = "array"))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseService.getAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get purchase order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Purchase order found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseOrderDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "404", description = "Purchase order not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponses.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        Optional<PurchaseOrder> order = purchaseService.getPurchaseOrderById(id);
        if (order.isEmpty()) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("PurchaseOrder not found");
        }
        return ResponseEntity.ok(order.get());
    }
}