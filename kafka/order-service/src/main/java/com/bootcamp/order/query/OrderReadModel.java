package com.bootcamp.order.query;

import com.bootcamp.order.model.OrderItem;
import com.bootcamp.order.model.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Read model for orders
 * 
 * Represents an optimized view for order queries.
 * Maintains eventual consistency with the write model
 * through asynchronous events.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Table("order_read_models")
public class OrderReadModel {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("status")
    private OrderStatus status;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("shipping_address")
    private String shippingAddress;

    @Column("notes")
    private String notes;

    @Column("is_deleted")
    private Boolean isDeleted;

    // Additional fields for optimized queries
    @Column("user_username")
    private String userUsername;

    @Column("user_email")
    private String userEmail;

    @Column("items_count")
    private Integer itemsCount;

    // Constructor por defecto
    public OrderReadModel() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.isDeleted = false;
    }

    // Constructor con parámetros
    public OrderReadModel(Long id, Long userId, BigDecimal totalAmount) {
        this();
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    @Override
    public String toString() {
        return "OrderReadModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", notes='" + notes + '\'' +
                ", isDeleted=" + isDeleted +
                ", userUsername='" + userUsername + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", itemsCount=" + itemsCount +
                '}';
    }
} 