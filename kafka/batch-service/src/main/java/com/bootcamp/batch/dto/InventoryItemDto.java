package com.bootcamp.batch.dto;

import com.bootcamp.batch.model.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryItemDto {
    
    private Long id;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;
    
    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
    
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;
    
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have at most 10 digits and 2 decimal places")
    private BigDecimal unitPrice;
    
    @Size(max = 255, message = "Supplier must not exceed 255 characters")
    private String supplier;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
    
    private ItemStatus status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private BigDecimal totalValue;
    
    private boolean lowStock;
    
    private boolean outOfStock;
    
    // Constructors
    public InventoryItemDto() {}
    
    public InventoryItemDto(String productCode, String productName, String category, 
                           Integer quantity, BigDecimal unitPrice, String supplier) {
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public ItemStatus getStatus() {
        return status;
    }
    
    public void setStatus(ItemStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public BigDecimal getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
    public boolean isLowStock() {
        return lowStock;
    }
    
    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }
    
    public boolean isOutOfStock() {
        return outOfStock;
    }
    
    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
    
    @Override
    public String toString() {
        return "InventoryItemDto{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", supplier='" + supplier + '\'' +
                ", status=" + status +
                '}';
    }
} 