package com.bootcamp.batch.controller;

import com.bootcamp.batch.dto.InventoryItemDto;
import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import com.bootcamp.batch.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping("/items")
    public ResponseEntity<List<InventoryItem>> getAllItems() {
        try {
            List<InventoryItem> items = inventoryService.getAllItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting all items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/{productCode}")
    public ResponseEntity<InventoryItem> getItemByProductCode(@PathVariable String productCode) {
        try {
            Optional<InventoryItem> item = inventoryService.getItemByProductCode(productCode);
            
            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting item by product code: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<InventoryItem>> getItemsByCategory(@PathVariable String category) {
        try {
            List<InventoryItem> items = inventoryService.getItemsByCategory(category);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting items by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/status/{status}")
    public ResponseEntity<List<InventoryItem>> getItemsByStatus(@PathVariable ItemStatus status) {
        try {
            List<InventoryItem> items = inventoryService.getItemsByStatus(status);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting items by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStockItems(
            @RequestParam(defaultValue = "10") int threshold) {
        try {
            List<InventoryItem> items = inventoryService.getLowStockItems(threshold);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting low stock items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/out-of-stock")
    public ResponseEntity<List<InventoryItem>> getOutOfStockItems() {
        try {
            List<InventoryItem> items = inventoryService.getOutOfStockItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting out of stock items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/items/expensive")
    public ResponseEntity<List<InventoryItem>> getExpensiveItems(
            @RequestParam(defaultValue = "1000.00") BigDecimal minPrice) {
        try {
            List<InventoryItem> items = inventoryService.getExpensiveItems(minPrice);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error getting expensive items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/items")
    public ResponseEntity<InventoryItem> createItem(@Valid @RequestBody InventoryItemDto itemDto) {
        try {
            InventoryItem createdItem = inventoryService.createItem(itemDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for creating item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/items/{productCode}")
    public ResponseEntity<InventoryItem> updateItem(
            @PathVariable String productCode,
            @Valid @RequestBody InventoryItemDto itemDto) {
        try {
            InventoryItem updatedItem = inventoryService.updateItem(productCode, itemDto);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for updating item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error updating item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/items/{productCode}")
    public ResponseEntity<Void> deleteItem(@PathVariable String productCode) {
        try {
            inventoryService.deleteItem(productCode);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for deleting item: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error deleting item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/items/{productCode}/stock")
    public ResponseEntity<InventoryItem> updateStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {
        try {
            inventoryService.updateStock(productCode, quantity);
            Optional<InventoryItem> item = inventoryService.getItemByProductCode(productCode);
            return ResponseEntity.ok(item.get());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for updating stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error updating stock: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/items/{productCode}/adjust")
    public ResponseEntity<InventoryItem> adjustStock(
            @PathVariable String productCode,
            @RequestParam int adjustment) {
        try {
            inventoryService.adjustStock(productCode, adjustment);
            Optional<InventoryItem> item = inventoryService.getItemByProductCode(productCode);
            return ResponseEntity.ok(item.get());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request for adjusting stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error adjusting stock: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getInventorySummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            List<InventoryItem> allItems = inventoryService.getAllItems();
            BigDecimal totalValue = inventoryService.getTotalInventoryValue();
            List<InventoryItem> lowStockItems = inventoryService.getLowStockItems(10);
            List<InventoryItem> outOfStockItems = inventoryService.getOutOfStockItems();
            
            summary.put("totalItems", allItems.size());
            summary.put("totalValue", totalValue);
            summary.put("lowStockItems", lowStockItems.size());
            summary.put("outOfStockItems", outOfStockItems.size());
            summary.put("activeItems", allItems.stream().filter(item -> item.getStatus() == ItemStatus.ACTIVE).count());
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error getting inventory summary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/analytics/total-value")
    public ResponseEntity<Map<String, Object>> getTotalInventoryValue() {
        try {
            Map<String, Object> response = new HashMap<>();
            BigDecimal totalValue = inventoryService.getTotalInventoryValue();
            response.put("totalValue", totalValue);
            response.put("currency", "USD");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting total inventory value: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/analytics/category/{category}/value")
    public ResponseEntity<Map<String, Object>> getTotalInventoryValueByCategory(@PathVariable String category) {
        try {
            Map<String, Object> response = new HashMap<>();
            BigDecimal totalValue = inventoryService.getTotalInventoryValueByCategory(category);
            response.put("category", category);
            response.put("totalValue", totalValue);
            response.put("currency", "USD");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting inventory value by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/analytics/suppliers")
    public ResponseEntity<List<Object[]>> getItemCountBySupplier() {
        try {
            List<Object[]> supplierStats = inventoryService.getItemCountBySupplier();
            return ResponseEntity.ok(supplierStats);
        } catch (Exception e) {
            logger.error("Error getting supplier statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/analytics/categories/prices")
    public ResponseEntity<List<Object[]>> getAveragePriceByCategory() {
        try {
            List<Object[]> categoryStats = inventoryService.getAveragePriceByCategory();
            return ResponseEntity.ok(categoryStats);
        } catch (Exception e) {
            logger.error("Error getting category price statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Inventory Service");
        health.put("timestamp", java.time.LocalDateTime.now());
        
        try {
            List<InventoryItem> allItems = inventoryService.getAllItems();
            health.put("totalItems", allItems.size());
            health.put("lowStockItems", inventoryService.getLowStockItems(10).size());
            health.put("outOfStockItems", inventoryService.getOutOfStockItems().size());
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }
} 