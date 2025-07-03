package com.bootcamp.order.controller;

import com.bootcamp.order.command.CreateOrderCommand;
import com.bootcamp.order.command.DeleteOrderCommand;
import com.bootcamp.order.command.OrderCommandService;
import com.bootcamp.order.command.UpdateOrderStatusCommand;
import com.bootcamp.order.dto.OrderRequest;
import com.bootcamp.order.dto.StatusRequest;
import com.bootcamp.order.model.Order;
import com.bootcamp.order.query.OrderQueryService;
import com.bootcamp.order.query.OrderReadModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Controller
 * 
 * REST API endpoints for order management operations.
 * Provides reactive CRUD operations for orders and handles HTTP requests/responses.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderCommandService orderCommandService;

    @Autowired
    private OrderQueryService orderQueryService;

    /**
     * Create a new order reactively
     * 
     * @param orderRequest the order creation request
     * @return ResponseEntity with the created order
     */
    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        logger.info("Received request to create order for user: {}", orderRequest.getUserId());

        CreateOrderCommand command = new CreateOrderCommand(
                orderRequest.getUserId(),
                orderRequest.getItems(),
                orderRequest.getShippingAddress(),
                orderRequest.getNotes()
        );

        return orderCommandService.executeCreateOrderCommand(command)
        .map(order -> {
            logger.info("Order created successfully with ID: {}", order.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        })
        .onErrorResume(RuntimeException.class, e -> {
            logger.error("Failed to create order: {}", e.getMessage());
            return Mono.just(ResponseEntity.badRequest().<Order>build());
        });
    }

    /**
     * Get all orders reactively using read model
     * 
     * @return Flux containing all active orders
     */
    @GetMapping
    public Flux<OrderReadModel> getAllOrders() {
        logger.info("Received request to get all orders");
        return orderQueryService.getAllActiveOrders()
                .doOnComplete(() -> logger.info("Retrieved all active orders"));
    }

    /**
     * Get order by ID reactively using read model
     * 
     * @param id the order ID
     * @return ResponseEntity with the order if found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderReadModel>> getOrderById(@PathVariable Long id) {
        logger.info("Received request to get order with ID: {}", id);

        return orderQueryService.getOrderById(id)
                .map(order -> {
                    logger.info("Order found with ID: {}", id);
                    return ResponseEntity.ok(order);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(response -> {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        logger.warn("Order not found with ID: {}", id);
                    }
                });
    }

    /**
     * Get orders by user ID reactively using read model
     * 
     * @param userId the user ID
     * @return Flux containing orders for the user
     */
    @GetMapping("/user/{userId}")
    public Flux<OrderReadModel> getOrdersByUserId(@PathVariable Long userId) {
        logger.info("Received request to get orders for user: {}", userId);
        return orderQueryService.getOrdersByUserId(userId)
                .doOnComplete(() -> logger.info("Retrieved orders for user: {}", userId));
    }

    /**
     * Update order status reactively using command
     * 
     * @param id the order ID
     * @param statusRequest the status update request
     * @return ResponseEntity with the updated order
     */
    @PutMapping("/{id}/status")
    public Mono<ResponseEntity<Order>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        logger.info("Received request to update order status for order: {} to: {}", id, statusRequest.getStatus());

        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(id, statusRequest.getStatus());

        return orderCommandService.executeUpdateOrderStatusCommand(command)
                .map(order -> {
                    logger.info("Order status updated successfully for order: {}", id);
                    return ResponseEntity.ok(order);
                })
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to update order status: {}", e.getMessage());
                    return Mono.just(ResponseEntity.notFound().<Order>build());
                });
    }

    /**
     * Delete order reactively using command
     * 
     * @param id the order ID to delete
     * @return ResponseEntity with no content if successful
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrder(@PathVariable Long id) {
        logger.info("Received request to delete order with ID: {}", id);

        DeleteOrderCommand command = new DeleteOrderCommand(id);

        return orderCommandService.executeDeleteOrderCommand(command)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnNext(response -> logger.info("Order deleted successfully with ID: {}", id))
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to delete order: {}", e.getMessage());
                    return Mono.just(ResponseEntity.notFound().<Void>build());
                });
    }

    /**
     * Get orders by status using read model
     * 
     * @param status the order status
     * @return Flux containing orders with the specified status
     */
    @GetMapping("/status/{status}")
    public Flux<OrderReadModel> getOrdersByStatus(@PathVariable String status) {
        logger.info("Received request to get orders with status: {}", status);
        return orderQueryService.getOrdersByStatus(status)
                .doOnComplete(() -> logger.info("Retrieved orders with status: {}", status));
    }

    /**
     * Get orders by date range using read model
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return Flux containing orders in the date range
     */
    @GetMapping("/date-range")
    public Flux<OrderReadModel> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        logger.info("Received request to get orders between {} and {}", startDate, endDate);
        
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        
        return orderQueryService.getOrdersByDateRange(start, end)
                .doOnComplete(() -> logger.info("Retrieved orders in date range"));
    }

    /**
     * Get orders by user and status using read model
     * 
     * @param userId the user ID
     * @param status the order status
     * @return Flux containing orders for the user with the specified status
     */
    @GetMapping("/user/{userId}/status/{status}")
    public Flux<OrderReadModel> getOrdersByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        logger.info("Received request to get orders for user {} with status: {}", userId, status);
        return orderQueryService.getOrdersByUserIdAndStatus(userId, status)
                .doOnComplete(() -> logger.info("Retrieved orders for user {} with status {}", userId, status));
    }

    /**
     * Get order statistics using read model
     * 
     * @return ResponseEntity with order statistics
     */
    @GetMapping("/statistics")
    public Mono<ResponseEntity<OrderQueryService.OrderStatistics>> getOrderStatistics() {
        logger.info("Received request to get order statistics");
        return orderQueryService.getOrderStatistics()
                .map(stats -> {
                    logger.info("Order statistics calculated: {}", stats);
                    return ResponseEntity.ok(stats);
                });
    }

    /**
     * Search orders by username using read model
     * 
     * @param username the username to search for
     * @return Flux containing orders with matching username
     */
    @GetMapping("/search/username")
    public Flux<OrderReadModel> searchOrdersByUsername(@RequestParam String username) {
        logger.info("Received request to search orders by username: {}", username);
        return orderQueryService.searchOrdersByUsername(username)
                .doOnComplete(() -> logger.info("Search completed for username: {}", username));
    }

    /**
     * Get orders with total amount greater than specified value
     * 
     * @param minAmount the minimum amount
     * @return Flux containing orders with total amount greater than minAmount
     */
    @GetMapping("/amount-greater-than")
    public Flux<OrderReadModel> getOrdersByTotalAmountGreaterThan(@RequestParam BigDecimal minAmount) {
        logger.info("Received request to get orders with amount greater than: {}", minAmount);
        return orderQueryService.getOrdersByTotalAmountGreaterThan(minAmount)
                .doOnComplete(() -> logger.info("Retrieved orders with amount greater than: {}", minAmount));
    }

    /**
     * Health check endpoint reactively
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        logger.debug("Health check requested");
        return Mono.just(ResponseEntity.ok("Order Service is running"));
    }
} 