package com.bootcamp.batch.processor;

import com.bootcamp.batch.dto.InventoryItemDto;
import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class InventoryItemProcessor implements ItemProcessor<InventoryItemDto, InventoryItem> {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemProcessor.class);
    
    @Override
    public InventoryItem process(InventoryItemDto item) throws Exception {
        // Validate the item
        validateItem(item);
        
        // Convert DTO to Entity
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setProductCode(item.getProductCode());
        inventoryItem.setProductName(item.getProductName());
        inventoryItem.setCategory(item.getCategory());
        inventoryItem.setQuantity(item.getQuantity());
        inventoryItem.setUnitPrice(item.getUnitPrice());
        inventoryItem.setSupplier(item.getSupplier());
        inventoryItem.setStatus(item.getStatus() != null ? item.getStatus() : ItemStatus.ACTIVE);
        inventoryItem.setCreatedAt(LocalDateTime.now());
        inventoryItem.setUpdatedAt(LocalDateTime.now());
        inventoryItem.setLastUpdated(LocalDateTime.now());
        
        return inventoryItem;
    }
    
    private void validateItem(InventoryItemDto itemDto) throws Exception {
        if (itemDto.getProductCode() == null || itemDto.getProductCode().trim().isEmpty()) {
            throw new Exception("Product code is required");
        }
        
        if (itemDto.getProductName() == null || itemDto.getProductName().trim().isEmpty()) {
            throw new Exception("Product name is required");
        }
        
        if (itemDto.getQuantity() == null || itemDto.getQuantity() < 0) {
            throw new Exception("Quantity must be non-negative");
        }
        
        if (itemDto.getUnitPrice() == null || itemDto.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Unit price must be greater than zero");
        }
    }
    
    private ItemStatus determineStatus(Integer quantity) {
        if (quantity == 0) {
            return ItemStatus.OUT_OF_STOCK;
        } else if (quantity <= 10) {
            return ItemStatus.LOW_STOCK;
        } else {
            return ItemStatus.ACTIVE;
        }
    }
    
    private void applyBusinessRules(InventoryItem item) {
        // Rule 1: Normalize category names
        String normalizedCategory = normalizeCategory(item.getCategory());
        item.setCategory(normalizedCategory);
        
        // Rule 2: Apply price rounding
        item.setUnitPrice(roundPrice(item.getUnitPrice()));
        
        // Rule 3: Set default supplier if not provided
        if (item.getSupplier() == null || item.getSupplier().trim().isEmpty()) {
            item.setSupplier("Default Supplier");
        }
        
        // Rule 4: Update status based on final quantity
        if (item.getQuantity() == 0) {
            item.setStatus(ItemStatus.OUT_OF_STOCK);
        } else if (item.getQuantity() <= 5) {
            item.setStatus(ItemStatus.LOW_STOCK);
        }
    }
    
    private String normalizeCategory(String category) {
        if (category == null) return "Uncategorized";
        
        String normalized = category.trim().toLowerCase();
        
        // Map common variations to standard categories
        if (normalized.contains("laptop") || normalized.contains("computer")) {
            return "Electronics";
        } else if (normalized.contains("phone") || normalized.contains("mobile")) {
            return "Electronics";
        } else if (normalized.contains("book") || normalized.contains("publication")) {
            return "Books";
        } else if (normalized.contains("desk") || normalized.contains("chair") || normalized.contains("office")) {
            return "Office";
        } else if (normalized.contains("cable") || normalized.contains("adapter") || normalized.contains("accessory")) {
            return "Electronics";
        } else if (normalized.contains("network") || normalized.contains("wifi") || normalized.contains("router")) {
            return "Electronics";
        }
        
        // Capitalize first letter of each word
        String[] words = normalized.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
    
    private BigDecimal roundPrice(BigDecimal price) {
        // Round to 2 decimal places
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
} 