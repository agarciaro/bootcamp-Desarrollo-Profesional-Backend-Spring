package com.bootcamp.order.repository;

import com.bootcamp.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order Repository Interface
 * 
 * Provides data access methods for Order entities.
 * Extends JpaRepository to get basic CRUD operations and adds custom query methods.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by user ID
     * 
     * @param userId the user ID to search for
     * @return List of orders for the user
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find orders by status
     * 
     * @param status the order status to search for
     * @return List of orders with the specified status
     */
    List<Order> findByStatus(String status);
} 