package com.bootcamp.order.event;

import java.time.LocalDateTime;

/**
 * Base event for orders
 * 
 * Represents an event that occurs in the order system.
 * Follows the Event Sourcing pattern to maintain a history
 * of all changes in the system.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public abstract class OrderEvent {
    
    private final String eventType;
    private final Long orderId;
    private final Long userId;
    private final LocalDateTime timestamp;
    
    public OrderEvent(String eventType, Long orderId, Long userId) {
        this.eventType = eventType;
        this.orderId = orderId;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getEventType() {
        return eventType;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "OrderEvent{" +
                "eventType='" + eventType + '\'' +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                '}';
    }
} 