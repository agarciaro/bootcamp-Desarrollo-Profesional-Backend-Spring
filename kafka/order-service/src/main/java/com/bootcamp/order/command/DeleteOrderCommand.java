package com.bootcamp.order.command;

/**
 * Command to delete an order
 * 
 * Follows the CQRS pattern to separate command operations
 * from query operations.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class DeleteOrderCommand {
    
    private final Long orderId;
    
    public DeleteOrderCommand(Long orderId) {
        this.orderId = orderId;
    }
    
    // Getters
    public Long getOrderId() {
        return orderId;
    }
    
    @Override
    public String toString() {
        return "DeleteOrderCommand{" +
                "orderId=" + orderId +
                '}';
    }
} 