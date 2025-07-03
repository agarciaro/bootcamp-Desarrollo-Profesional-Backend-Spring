package com.bootcamp.order.command;

import com.bootcamp.order.model.OrderItem;
import java.util.List;

/**
 * Command to create a new order
 * 
 * Follows the CQRS pattern to separate command operations
 * from query operations.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class CreateOrderCommand {
    
    private final Long userId;
    private final List<OrderItem> items;
    private final String shippingAddress;
    private final String notes;
    
    public CreateOrderCommand(Long userId, List<OrderItem> items, String shippingAddress, String notes) {
        this.userId = userId;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.notes = notes;
    }
    
    // Getters
    public Long getUserId() {
        return userId;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getNotes() {
        return notes;
    }
    
    @Override
    public String toString() {
        return "CreateOrderCommand{" +
                "userId=" + userId +
                ", items=" + items +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
} 