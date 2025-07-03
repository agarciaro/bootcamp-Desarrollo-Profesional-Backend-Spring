package com.bootcamp.batch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_item")
public class InventoryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    @Column(name = "category", nullable = false)
    private String category;
    
    @Min(value = 0, message = "Quantity must be non-negative")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have at most 10 digits and 2 decimal places")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Size(max = 255, message = "Supplier must not exceed 255 characters")
    @Column(name = "supplier")
    private String supplier;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ItemStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public InventoryItem() {
        this.status = ItemStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public InventoryItem(String productCode, String productName, String category, 
                        Integer quantity, BigDecimal unitPrice, String supplier) {
        this();
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
    
    // Business methods
    public BigDecimal getTotalValue() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public boolean isLowStock() {
        return quantity <= 10;
    }
    
    public boolean isOutOfStock() {
        return quantity == 0;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "InventoryItem{" +
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