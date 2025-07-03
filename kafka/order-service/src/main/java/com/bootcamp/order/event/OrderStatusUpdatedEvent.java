package com.bootcamp.order.event;

/**
 * Event that is published when the status of an order is updated
 * 
 * Contains the status change information to maintain
 * eventual consistency in the read model.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class OrderStatusUpdatedEvent extends OrderEvent {
    
    private final String oldStatus;
    private final String newStatus;
    
    public OrderStatusUpdatedEvent(Long orderId, Long userId, String oldStatus, String newStatus) {
        super("ORDER_STATUS_UPDATED", orderId, userId);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    // Getters
    public String getOldStatus() {
        return oldStatus;
    }
    
    public String getNewStatus() {
        return newStatus;
    }
    
    @Override
    public String toString() {
        return "OrderStatusUpdatedEvent{" +
                "eventType='" + getEventType() + '\'' +
                ", orderId=" + getOrderId() +
                ", userId=" + getUserId() +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
} 