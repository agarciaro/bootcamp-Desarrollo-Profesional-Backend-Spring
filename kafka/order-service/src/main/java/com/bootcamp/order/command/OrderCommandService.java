package com.bootcamp.order.command;

import com.bootcamp.order.client.UserServiceClient;
import com.bootcamp.order.client.UserDto;
import com.bootcamp.order.event.OrderCreatedEvent;
import com.bootcamp.order.event.OrderDeletedEvent;
import com.bootcamp.order.event.OrderStatusUpdatedEvent;
import com.bootcamp.order.model.Order;
import com.bootcamp.order.model.OrderItem;
import com.bootcamp.order.model.OrderStatus;
import com.bootcamp.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

/**
 * Command service for orders
 * 
 * Handles all write operations (commands) following
 * the CQRS pattern. Publishes events to maintain eventual
 * consistency with the read model.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class OrderCommandService {

    private static final Logger logger = LoggerFactory.getLogger(OrderCommandService.class);
    private static final String ORDER_EVENTS_TOPIC = "order-events";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Execute command to create a new order
     * 
     * @param command the creation command
     * @return Mono with the created order
     */
    public Mono<Order> executeCreateOrderCommand(CreateOrderCommand command) {
        logger.info("Executing order creation command: {}", command);

        return validateUser(command.getUserId())
                .flatMap(user -> {
                    // Calculate total amount
                    BigDecimal totalAmount = command.getItems().stream()
                            .map(OrderItem::getTotalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    // Create order
                    Order order = new Order(command.getUserId(), totalAmount);
                    order.setShippingAddress(command.getShippingAddress());
                    order.setNotes(command.getNotes());

                    return Mono.just(order);
                })
                .flatMap(orderRepository::save)
                .doOnNext(savedOrder -> {
                    logger.info("Order created successfully with ID: {}", savedOrder.getId());
                    publishOrderCreatedEvent(savedOrder, command.getItems());
                });
    }

    /**
     * Execute command to update order status
     * 
     * @param command the status update command
     * @return Mono with the updated order
     */
    public Mono<Order> executeUpdateOrderStatusCommand(UpdateOrderStatusCommand command) {
        logger.info("Executing order status update command: {}", command);

        return orderRepository.findById(command.getOrderId())
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found with ID: " + command.getOrderId())))
                .map(order -> {
                    String oldStatus = order.getStatus().toString();
                    order.setStatus(OrderStatus.valueOf(command.getNewStatus().toUpperCase()));
                    order.preUpdate();
                    return new OrderStatusUpdateResult(order, oldStatus);
                })
                .flatMap(result -> orderRepository.save(result.getOrder())
                        .doOnNext(savedOrder -> {
                            logger.info("Order status updated successfully: {}", savedOrder.getId());
                            publishOrderStatusUpdatedEvent(savedOrder, result.getOldStatus());
                        }));
    }

    /**
     * Execute command to delete an order
     * 
     * @param command the deletion command
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> executeDeleteOrderCommand(DeleteOrderCommand command) {
        logger.info("Executing order deletion command: {}", command);

        return orderRepository.findById(command.getOrderId())
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found with ID: " + command.getOrderId())))
                .doOnNext(order -> {
                    logger.info("Order found for deletion: {}", order.getId());
                    publishOrderDeletedEvent(order);
                })
                .then(orderRepository.deleteById(command.getOrderId()))
                .doOnSuccess(v -> logger.info("Order deleted successfully with ID: {}", command.getOrderId()));
    }

    /**
     * Validate that the user exists
     * 
     * @param userId the user ID to validate
     * @return Mono with user information if valid
     */
    private Mono<UserDto> validateUser(Long userId) {
        return userServiceClient.getUserById(userId)
                .onErrorResume(e -> {
                    logger.error("Error validating user: {}", e.getMessage());
                    return Mono.error(new RuntimeException("User not found with ID: " + userId));
                });
    }

    /**
     * Publish order created event
     * 
     * @param order the created order
     * @param items the order items
     */
    private void publishOrderCreatedEvent(Order order, List<OrderItem> items) {
        try {
            OrderCreatedEvent event = new OrderCreatedEvent(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalAmount(),
                    items,
                    order.getShippingAddress(),
                    order.getNotes()
            );
            kafkaTemplate.send(ORDER_EVENTS_TOPIC, event);
            logger.info("Order created event published: {}", event);
        } catch (Exception e) {
            logger.error("Error publishing order created event: {}", e.getMessage(), e);
        }
    }

    /**
     * Publish order status updated event
     * 
     * @param order the updated order
     * @param oldStatus the previous status
     */
    private void publishOrderStatusUpdatedEvent(Order order, String oldStatus) {
        try {
            OrderStatusUpdatedEvent event = new OrderStatusUpdatedEvent(
                    order.getId(),
                    order.getUserId(),
                    oldStatus,
                    order.getStatus().toString()
            );
            kafkaTemplate.send(ORDER_EVENTS_TOPIC, event);
            logger.info("Order status updated event published: {}", event);
        } catch (Exception e) {
            logger.error("Error publishing order status updated event: {}", e.getMessage(), e);
        }
    }

    /**
     * Publish order deleted event
     * 
     * @param order the deleted order
     */
    private void publishOrderDeletedEvent(Order order) {
        try {
            OrderDeletedEvent event = new OrderDeletedEvent(order.getId(), order.getUserId());
            kafkaTemplate.send(ORDER_EVENTS_TOPIC, event);
            logger.info("Order deleted event published: {}", event);
        } catch (Exception e) {
            logger.error("Error publishing order deleted event: {}", e.getMessage(), e);
        }
    }

    /**
     * Helper class to maintain previous state during update
     */
    private static class OrderStatusUpdateResult {
        private final Order order;
        private final String oldStatus;

        public OrderStatusUpdateResult(Order order, String oldStatus) {
            this.order = order;
            this.oldStatus = oldStatus;
        }

        public Order getOrder() {
            return order;
        }

        public String getOldStatus() {
            return oldStatus;
        }
    }
} 