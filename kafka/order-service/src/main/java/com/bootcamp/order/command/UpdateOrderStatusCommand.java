package com.bootcamp.order.command;

/**
 * Command to update the status of an order
 * 
 * Follows the CQRS pattern to separate command operations
 * from query operations.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class UpdateOrderStatusCommand {
    
    private final Long orderId;
    private final String newStatus;
    
    public UpdateOrderStatusCommand(Long orderId, String newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }
    
    // Getters
    public Long getOrderId() {
        return orderId;
    }
    
    public String getNewStatus() {
        return newStatus;
    }
    
    @Override
    public String toString() {
        return "UpdateOrderStatusCommand{" +
                "orderId=" + orderId +
                ", newStatus='" + newStatus + '\'' +
                '}';
    }
} 