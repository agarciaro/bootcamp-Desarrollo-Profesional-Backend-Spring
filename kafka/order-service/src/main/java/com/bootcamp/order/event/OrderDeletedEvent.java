package com.bootcamp.order.event;

/**
 * Event that is published when an order is deleted
 * 
 * Notifies the read model that it should mark the order
 * as deleted to maintain eventual consistency.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class OrderDeletedEvent extends OrderEvent {
    
    public OrderDeletedEvent(Long orderId, Long userId) {
        super("ORDER_DELETED", orderId, userId);
    }
    
    @Override
    public String toString() {
        return "OrderDeletedEvent{" +
                "eventType='" + getEventType() + '\'' +
                ", orderId=" + getOrderId() +
                ", userId=" + getUserId() +
                ", timestamp=" + getTimestamp() +
                '}';
    }
} 