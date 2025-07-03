package com.bootcamp.order.event;

import com.bootcamp.order.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

/**
 * Event that is published when a new order is created
 * 
 * Contains all the information necessary to reconstruct
 * the order state in the read model.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class OrderCreatedEvent extends OrderEvent {
    
    private final BigDecimal totalAmount;
    private final List<OrderItem> items;
    private final String shippingAddress;
    private final String notes;
    
    public OrderCreatedEvent(Long orderId, Long userId, BigDecimal totalAmount, 
                           List<OrderItem> items, String shippingAddress, String notes) {
        super("ORDER_CREATED", orderId, userId);
        this.totalAmount = totalAmount;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.notes = notes;
    }
    
    // Getters
    public BigDecimal getTotalAmount() {
        return totalAmount;
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
        return "OrderCreatedEvent{" +
                "eventType='" + getEventType() + '\'' +
                ", orderId=" + getOrderId() +
                ", userId=" + getUserId() +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", notes='" + notes + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
} 