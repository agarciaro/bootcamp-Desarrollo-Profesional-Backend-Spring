package com.bootcamp.order.dto;

import com.bootcamp.order.model.OrderItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Order Request DTO
 * 
 * Data transfer object for order creation requests.
 * Contains all necessary information to create a new order.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class OrderRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotEmpty(message = "Order items are required")
    private List<OrderItem> items;
    
    private String shippingAddress;
    
    private String notes;

    // Default constructor
    public OrderRequest() {
    }

    // Constructor with parameters
    public OrderRequest(Long userId, List<OrderItem> items, String shippingAddress, String notes) {
        this.userId = userId;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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

    @Override
    public String toString() {
        return "OrderRequest{" +
                "userId=" + userId +
                ", items=" + items +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
} 