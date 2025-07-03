package com.bootcamp.order.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Query service for orders
 * 
 * Handles all read operations (queries) using
 * the optimized read model that maintains eventual
 * consistency with the write model.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class OrderQueryService {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryService.class);

    @Autowired
    private OrderReadModelRepository orderReadModelRepository;

    /**
     * Get all active orders
     * 
     * @return Flux with all non-deleted orders
     */
    public Flux<OrderReadModel> getAllActiveOrders() {
        logger.info("Querying all active orders");
        return orderReadModelRepository.findAllActive()
                .doOnComplete(() -> logger.info("Active orders query completed"));
    }

    /**
     * Get order by ID
     * 
     * @param id the order ID
     * @return Mono with the order if it exists and is not deleted
     */
    public Mono<OrderReadModel> getOrderById(Long id) {
        logger.info("Querying order with ID: {}", id);
        return orderReadModelRepository.findById(id)
                .filter(order -> !order.getIsDeleted())
                .doOnNext(order -> logger.info("Order found: {}", order.getId()))
                .doOnError(e -> logger.error("Error querying order: {}", e.getMessage()));
    }

    /**
     * Get orders by user
     * 
     * @param userId the user ID
     * @return Flux with the user's orders
     */
    public Flux<OrderReadModel> getOrdersByUserId(Long userId) {
        logger.info("Querying orders for user: {}", userId);
        return orderReadModelRepository.findByUserId(userId)
                .doOnComplete(() -> logger.info("User orders query completed: {}", userId));
    }

    /**
     * Get orders by status
     * 
     * @param status the order status
     * @return Flux with orders of the specified status
     */
    public Flux<OrderReadModel> getOrdersByStatus(String status) {
        logger.info("Querying orders by status: {}", status);
        return orderReadModelRepository.findByStatus(status)
                .doOnComplete(() -> logger.info("Orders by status query completed: {}", status));
    }

    /**
     * Get orders by date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return Flux with orders in the date range
     */
    public Flux<OrderReadModel> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Querying orders by date range: {} - {}", startDate, endDate);
        return orderReadModelRepository.findByDateRange(startDate.toString(), endDate.toString())
                .doOnComplete(() -> logger.info("Orders by date range query completed"));
    }

    /**
     * Get orders by user and status
     * 
     * @param userId the user ID
     * @param status the order status
     * @return Flux with orders for the user and specified status
     */
    public Flux<OrderReadModel> getOrdersByUserIdAndStatus(Long userId, String status) {
        logger.info("Querying orders for user {} with status: {}", userId, status);
        return orderReadModelRepository.findByUserIdAndStatus(userId, status)
                .doOnComplete(() -> logger.info("Orders by user and status query completed"));
    }

    /**
     * Count orders by user
     * 
     * @param userId the user ID
     * @return Mono with the number of orders for the user
     */
    public Mono<Long> countOrdersByUserId(Long userId) {
        logger.info("Counting orders for user: {}", userId);
        return orderReadModelRepository.countByUserId(userId)
                .doOnNext(count -> logger.info("User {} has {} orders", userId, count));
    }

    /**
     * Count orders by status
     * 
     * @param status the order status
     * @return Mono with the number of orders with the specified status
     */
    public Mono<Long> countOrdersByStatus(String status) {
        logger.info("Counting orders by status: {}", status);
        return orderReadModelRepository.countByStatus(status)
                .doOnNext(count -> logger.info("There are {} orders with status {}", count, status));
    }

    /**
     * Get orders with total amount greater than specified
     * 
     * @param minAmount minimum amount
     * @return Flux with orders that exceed the minimum amount
     */
    public Flux<OrderReadModel> getOrdersByTotalAmountGreaterThan(BigDecimal minAmount) {
        logger.info("Querying orders with amount greater than: {}", minAmount);
        return orderReadModelRepository.findByTotalAmountGreaterThan(minAmount.toString())
                .doOnComplete(() -> logger.info("Orders by amount query completed"));
    }

    /**
     * Search orders by username
     * 
     * @param username the username to search for
     * @return Flux with orders that contain the username
     */
    public Flux<OrderReadModel> searchOrdersByUsername(String username) {
        logger.info("Searching orders by username: {}", username);
        return orderReadModelRepository.findByUserUsernameContaining(username)
                .doOnComplete(() -> logger.info("Orders search by username completed"));
    }

    /**
     * Get order statistics
     * 
     * @return Mono with basic order statistics
     */
    public Mono<OrderStatistics> getOrderStatistics() {
        logger.info("Calculating order statistics");
        
        return Mono.zip(
                orderReadModelRepository.countByStatus("PENDING"),
                orderReadModelRepository.countByStatus("CONFIRMED"),
                orderReadModelRepository.countByStatus("SHIPPED"),
                orderReadModelRepository.countByStatus("DELIVERED"),
                orderReadModelRepository.countByStatus("CANCELLED")
        ).map(tuple -> new OrderStatistics(
                tuple.getT1(), // pending
                tuple.getT2(), // confirmed
                tuple.getT3(), // shipped
                tuple.getT4(), // delivered
                tuple.getT5()  // cancelled
        )).doOnNext(stats -> logger.info("Statistics calculated: {}", stats));
    }

    /**
     * Class to represent order statistics
     */
    public static class OrderStatistics {
        private final Long pendingCount;
        private final Long confirmedCount;
        private final Long shippedCount;
        private final Long deliveredCount;
        private final Long cancelledCount;

        public OrderStatistics(Long pendingCount, Long confirmedCount, Long shippedCount, 
                             Long deliveredCount, Long cancelledCount) {
            this.pendingCount = pendingCount;
            this.confirmedCount = confirmedCount;
            this.shippedCount = shippedCount;
            this.deliveredCount = deliveredCount;
            this.cancelledCount = cancelledCount;
        }

        // Getters
        public Long getPendingCount() { return pendingCount; }
        public Long getConfirmedCount() { return confirmedCount; }
        public Long getShippedCount() { return shippedCount; }
        public Long getDeliveredCount() { return deliveredCount; }
        public Long getCancelledCount() { return cancelledCount; }
        public Long getTotalCount() { 
            return pendingCount + confirmedCount + shippedCount + deliveredCount + cancelledCount; 
        }

        @Override
        public String toString() {
            return "OrderStatistics{" +
                    "pending=" + pendingCount +
                    ", confirmed=" + confirmedCount +
                    ", shipped=" + shippedCount +
                    ", delivered=" + deliveredCount +
                    ", cancelled=" + cancelledCount +
                    ", total=" + getTotalCount() +
                    '}';
        }
    }
} 