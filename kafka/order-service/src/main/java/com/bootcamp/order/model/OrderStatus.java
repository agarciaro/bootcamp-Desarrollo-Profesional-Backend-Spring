package com.bootcamp.order.model;

/**
 * Order Status Enumeration
 * 
 * Defines the possible states of an order.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
} 