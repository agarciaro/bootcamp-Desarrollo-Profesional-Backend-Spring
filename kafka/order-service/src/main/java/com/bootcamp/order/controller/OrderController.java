package com.bootcamp.order.controller;

import com.bootcamp.order.model.Order;
import com.bootcamp.order.model.OrderItem;
import com.bootcamp.order.service.OrderService;
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
import java.util.List;

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
    private OrderService orderService;

    /**
     * Create a new order reactively
     * 
     * @param orderRequest the order creation request
     * @return ResponseEntity with the created order
     */
    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        logger.info("Received request to create order for user: {}", orderRequest.getUserId());

        return orderService.createOrder(
                orderRequest.getUserId(),
                orderRequest.getItems(),
                orderRequest.getShippingAddress(),
                orderRequest.getNotes()
        )
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
     * Get all orders reactively
     * 
     * @return Flux containing all orders
     */
    @GetMapping
    public Flux<Order> getAllOrders() {
        logger.info("Received request to get all orders");
        return orderService.getAllOrders()
                .doOnComplete(() -> logger.info("Retrieved all orders"));
    }

    /**
     * Get order by ID reactively
     * 
     * @param id the order ID
     * @return ResponseEntity with the order if found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable Long id) {
        logger.info("Received request to get order with ID: {}", id);

        return orderService.getOrderById(id)
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
     * Get orders by user ID reactively
     * 
     * @param userId the user ID
     * @return Flux containing orders for the user
     */
    @GetMapping("/user/{userId}")
    public Flux<Order> getOrdersByUserId(@PathVariable Long userId) {
        logger.info("Received request to get orders for user: {}", userId);
        return orderService.getOrdersByUserId(userId)
                .doOnComplete(() -> logger.info("Retrieved orders for user: {}", userId));
    }

    /**
     * Update order status reactively
     * 
     * @param id the order ID
     * @param statusRequest the status update request
     * @return ResponseEntity with the updated order
     */
    @PutMapping("/{id}/status")
    public Mono<ResponseEntity<Order>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        logger.info("Received request to update order status for order: {} to: {}", id, statusRequest.getStatus());

        return orderService.updateOrderStatus(id, statusRequest.getStatus())
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
     * Delete order reactively
     * 
     * @param id the order ID to delete
     * @return ResponseEntity with no content if successful
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrder(@PathVariable Long id) {
        logger.info("Received request to delete order with ID: {}", id);

        return orderService.deleteOrder(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnNext(response -> logger.info("Order deleted successfully with ID: {}", id))
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to delete order: {}", e.getMessage());
                    return Mono.just(ResponseEntity.notFound().<Void>build());
                });
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

/**
 * Order Request DTO
 * 
 * Data transfer object for order creation requests.
 */
class OrderRequest {
    private Long userId;
    private List<OrderItem> items;
    private String shippingAddress;
    private String notes;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

/**
 * Status Request DTO
 * 
 * Data transfer object for status update requests.
 */
class StatusRequest {
    private String status;

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 