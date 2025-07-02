package com.bootcamp.order.service;

import com.bootcamp.order.client.UserServiceClient;
import com.bootcamp.order.model.Order;
import com.bootcamp.order.model.OrderItem;
import com.bootcamp.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Service
 * 
 * Contains reactive business logic for order management operations.
 * Handles order CRUD operations and integrates with User Service via Feign client.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * Create a new order reactively
     * 
     * @param userId the user ID
     * @param items the order items
     * @param shippingAddress the shipping address
     * @param notes additional notes
     * @return Mono containing the created order
     */
    public Mono<Order> createOrder(Long userId, List<OrderItem> items, String shippingAddress, String notes) {
        logger.info("Creating new order for user: {}", userId);

        return validateUser(userId)
                .flatMap(user -> {
                    // Calculate total amount
                    BigDecimal totalAmount = items.stream()
                            .map(OrderItem::getTotalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Create order
                    Order order = new Order(userId, totalAmount);
                    order.setShippingAddress(shippingAddress);
                    order.setNotes(notes);

                    // Set order reference for items
                    items.forEach(item -> item.setOrder(order));
                    order.setOrderItems(items);

                    return Mono.just(order);
                })
                .publishOn(Schedulers.boundedElastic())
                .map(orderRepository::save)
                .doOnNext(savedOrder -> logger.info("Order created successfully with ID: {}", savedOrder.getId()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get all orders reactively
     * 
     * @return Flux containing all orders
     */
    public Flux<Order> getAllOrders() {
        logger.info("Retrieving all orders");
        return Flux.fromIterable(orderRepository.findAll())
                .publishOn(Schedulers.boundedElastic())
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get order by ID reactively
     * 
     * @param id the order ID
     * @return Mono containing the order if found
     */
    public Mono<Order> getOrderById(Long id) {
        logger.info("Retrieving order with ID: {}", id);
        return Mono.fromCallable(() -> orderRepository.findById(id))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.empty()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get orders by user ID reactively
     * 
     * @param userId the user ID
     * @return Flux containing orders for the user
     */
    public Flux<Order> getOrdersByUserId(Long userId) {
        logger.info("Retrieving orders for user: {}", userId);
        return Flux.fromIterable(orderRepository.findByUserId(userId))
                .publishOn(Schedulers.boundedElastic())
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Update order status reactively
     * 
     * @param id the order ID
     * @param status the new status
     * @return Mono containing the updated order
     */
    public Mono<Order> updateOrderStatus(Long id, String status) {
        logger.info("Updating order status for order: {} to: {}", id, status);

        return Mono.fromCallable(() -> orderRepository.findById(id))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.error(new RuntimeException("Order not found with ID: " + id))))
                .map(order -> {
                    order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
                    return order;
                })
                .map(orderRepository::save)
                .doOnNext(updatedOrder -> logger.info("Order status updated successfully for order: {}", updatedOrder.getId()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Delete order reactively
     * 
     * @param id the order ID to delete
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> deleteOrder(Long id) {
        logger.info("Deleting order with ID: {}", id);

        return Mono.fromCallable(() -> orderRepository.findById(id))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(optional -> optional.map(Mono::just)
                        .orElse(Mono.error(new RuntimeException("Order not found with ID: " + id))))
                .then(Mono.fromRunnable(() -> orderRepository.deleteById(id)))
                .doOnSuccess(v -> logger.info("Order deleted successfully with ID: {}", id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Validate user exists reactively
     * 
     * @param userId the user ID to validate
     * @return Mono containing user information if valid
     */
    private Mono<UserServiceClient.UserDto> validateUser(Long userId) {
        return Mono.fromCallable(() -> userServiceClient.getUserById(userId))
                .publishOn(Schedulers.boundedElastic())
                .onErrorResume(e -> {
                    logger.error("Failed to validate user: {}", e.getMessage());
                    return Mono.error(new RuntimeException("User not found with ID: " + userId));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}

/**
 * Order Status Enumeration
 * 
 * Defines the possible states of an order.
 */
enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
} 