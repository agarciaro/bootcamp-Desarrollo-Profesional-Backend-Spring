package com.bootcamp.order.repository;

import com.bootcamp.order.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Order Repository Interface
 * 
 * Provides reactive data access methods for Order entities.
 * Extends ReactiveCrudRepository to get basic CRUD operations and adds custom query methods.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    /**
     * Find orders by user ID
     * 
     * @param userId the user ID to search for
     * @return Flux of orders for the user
     */
    Flux<Order> findByUserId(Long userId);

    /**
     * Find orders by status
     * 
     * @param status the order status to search for
     * @return Flux of orders with the specified status
     */
    Flux<Order> findByStatus(String status);
} 