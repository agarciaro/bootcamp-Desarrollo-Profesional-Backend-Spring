package com.bootcamp.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Status Request DTO
 * 
 * Data transfer object for status update requests.
 * Contains the new status to be applied to an order.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class StatusRequest {
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED)$", 
             message = "Status must be one of: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED")
    private String status;

    // Default constructor
    public StatusRequest() {
    }

    // Constructor with parameters
    public StatusRequest(String status) {
        this.status = status;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusRequest{" +
                "status='" + status + '\'' +
                '}';
    }
} 