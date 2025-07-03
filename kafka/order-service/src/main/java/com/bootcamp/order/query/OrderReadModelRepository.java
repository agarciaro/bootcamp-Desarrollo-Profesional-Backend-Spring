package com.bootcamp.order.query;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository for the order read model
 * 
 * Provides optimized methods for order queries
 * that maintain eventual consistency.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Repository
public interface OrderReadModelRepository extends ReactiveCrudRepository<OrderReadModel, Long> {

    /**
     * Find all non-deleted orders
     */
    @Query("SELECT * FROM order_read_models WHERE is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findAllActive();

    /**
     * Find orders by user (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE user_id = :userId AND is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findByUserId(Long userId);

    /**
     * Find orders by status (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE status = :status AND is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findByStatus(String status);

    /**
     * Find orders by date range (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE created_at BETWEEN :startDate AND :endDate AND is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findByDateRange(String startDate, String endDate);

    /**
     * Find orders by user and status (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE user_id = :userId AND status = :status AND is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findByUserIdAndStatus(Long userId, String status);

    /**
     * Count orders by user (non-deleted)
     */
    @Query("SELECT COUNT(*) FROM order_read_models WHERE user_id = :userId AND is_deleted = false")
    Mono<Long> countByUserId(Long userId);

    /**
     * Count orders by status (non-deleted)
     */
    @Query("SELECT COUNT(*) FROM order_read_models WHERE status = :status AND is_deleted = false")
    Mono<Long> countByStatus(String status);

    /**
     * Find orders with total amount greater than specified (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE total_amount > :minAmount AND is_deleted = false ORDER BY total_amount DESC")
    Flux<OrderReadModel> findByTotalAmountGreaterThan(String minAmount);

    /**
     * Find orders by username (non-deleted)
     */
    @Query("SELECT * FROM order_read_models WHERE user_username LIKE CONCAT('%', :username, '%') AND is_deleted = false ORDER BY created_at DESC")
    Flux<OrderReadModel> findByUserUsernameContaining(String username);
} 