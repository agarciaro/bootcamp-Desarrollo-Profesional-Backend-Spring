package com.bootcamp.batch.service;

import com.bootcamp.batch.dto.InventoryItemDto;
import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import com.bootcamp.batch.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }
    
    public Optional<InventoryItem> getItemByProductCode(String productCode) {
        return inventoryItemRepository.findByProductCode(productCode);
    }
    
    public List<InventoryItem> getItemsByCategory(String category) {
        return inventoryItemRepository.findByCategory(category);
    }
    
    public List<InventoryItem> getItemsByStatus(ItemStatus status) {
        return inventoryItemRepository.findByStatus(status);
    }
    
    public List<InventoryItem> getLowStockItems(int threshold) {
        return inventoryItemRepository.findLowStockItems(threshold);
    }
    
    public List<InventoryItem> getOutOfStockItems() {
        return inventoryItemRepository.findOutOfStockItems();
    }
    
    public List<InventoryItem> getExpensiveItems(BigDecimal minPrice) {
        return inventoryItemRepository.findExpensiveItems(minPrice);
    }
    
    public InventoryItem createItem(InventoryItemDto itemDto) {
        logger.info("Creating new inventory item: {}", itemDto.getProductCode());
        
        // Check if item already exists
        if (inventoryItemRepository.existsByProductCode(itemDto.getProductCode())) {
            throw new IllegalArgumentException("Product with code " + itemDto.getProductCode() + " already exists");
        }
        
        // Create new item
        InventoryItem item = new InventoryItem();
        item.setProductCode(itemDto.getProductCode().trim().toUpperCase());
        item.setProductName(itemDto.getProductName().trim());
        item.setCategory(itemDto.getCategory().trim());
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(itemDto.getUnitPrice());
        item.setSupplier(itemDto.getSupplier() != null ? itemDto.getSupplier().trim() : "Default Supplier");
        item.setStatus(determineStatus(itemDto.getQuantity()));
        
        InventoryItem savedItem = inventoryItemRepository.save(item);
        logger.info("Created inventory item: {} with ID: {}", savedItem.getProductCode(), savedItem.getId());
        
        return savedItem;
    }
    
    public InventoryItem updateItem(String productCode, InventoryItemDto itemDto) {
        logger.info("Updating inventory item: {}", productCode);
        
        Optional<InventoryItem> existingItem = inventoryItemRepository.findByProductCode(productCode);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Product with code " + productCode + " not found");
        }
        
        InventoryItem item = existingItem.get();
        item.setProductName(itemDto.getProductName().trim());
        item.setCategory(itemDto.getCategory().trim());
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(itemDto.getUnitPrice());
        item.setSupplier(itemDto.getSupplier() != null ? itemDto.getSupplier().trim() : item.getSupplier());
        item.setStatus(determineStatus(itemDto.getQuantity()));
        item.setUpdatedAt(LocalDateTime.now());
        item.setLastUpdated(LocalDateTime.now());
        
        InventoryItem updatedItem = inventoryItemRepository.save(item);
        logger.info("Updated inventory item: {} with ID: {}", updatedItem.getProductCode(), updatedItem.getId());
        
        return updatedItem;
    }
    
    public void deleteItem(String productCode) {
        logger.info("Deleting inventory item: {}", productCode);
        
        if (!inventoryItemRepository.existsByProductCode(productCode)) {
            throw new IllegalArgumentException("Product with code " + productCode + " not found");
        }
        
        inventoryItemRepository.deleteByProductCode(productCode);
        logger.info("Deleted inventory item: {}", productCode);
    }
    
    public void updateStock(String productCode, int newQuantity) {
        logger.info("Updating stock for product: {} to quantity: {}", productCode, newQuantity);
        
        Optional<InventoryItem> existingItem = inventoryItemRepository.findByProductCode(productCode);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Product with code " + productCode + " not found");
        }
        
        InventoryItem item = existingItem.get();
        item.setQuantity(newQuantity);
        item.setStatus(determineStatus(newQuantity));
        item.setUpdatedAt(LocalDateTime.now());
        item.setLastUpdated(LocalDateTime.now());
        
        inventoryItemRepository.save(item);
        logger.info("Updated stock for product: {} to quantity: {}", productCode, newQuantity);
    }
    
    public void adjustStock(String productCode, int adjustment) {
        logger.info("Adjusting stock for product: {} by: {}", productCode, adjustment);
        
        Optional<InventoryItem> existingItem = inventoryItemRepository.findByProductCode(productCode);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Product with code " + productCode + " not found");
        }
        
        InventoryItem item = existingItem.get();
        int newQuantity = item.getQuantity() + adjustment;
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock adjustment would result in negative quantity");
        }
        
        item.setQuantity(newQuantity);
        item.setStatus(determineStatus(newQuantity));
        item.setUpdatedAt(LocalDateTime.now());
        item.setLastUpdated(LocalDateTime.now());
        
        inventoryItemRepository.save(item);
        logger.info("Adjusted stock for product: {} to quantity: {}", productCode, newQuantity);
    }
    
    public BigDecimal getTotalInventoryValue() {
        BigDecimal totalValue = inventoryItemRepository.getTotalInventoryValue();
        return totalValue != null ? totalValue : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalInventoryValueByCategory(String category) {
        BigDecimal totalValue = inventoryItemRepository.getTotalInventoryValueByCategory(category);
        return totalValue != null ? totalValue : BigDecimal.ZERO;
    }
    
    public Long getItemCountByCategory(String category) {
        return inventoryItemRepository.countByCategory(category);
    }
    
    public List<Object[]> getItemCountBySupplier() {
        return inventoryItemRepository.getItemCountBySupplier();
    }
    
    public List<Object[]> getAveragePriceByCategory() {
        return inventoryItemRepository.getAveragePriceByCategory();
    }
    
    public List<InventoryItem> getItemsNotUpdatedSince(LocalDateTime date) {
        return inventoryItemRepository.findItemsNotUpdatedSince(date);
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
} 