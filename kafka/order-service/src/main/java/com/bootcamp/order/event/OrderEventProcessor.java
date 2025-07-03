package com.bootcamp.order.event;

import com.bootcamp.order.client.UserServiceClient;
import com.bootcamp.order.client.UserDto;
import com.bootcamp.order.query.OrderReadModel;
import com.bootcamp.order.query.OrderReadModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Order event processor
 * 
 * Listens to order events and updates the read model
 * to maintain eventual consistency. This component is
 * responsible for synchronizing the write model with the
 * read model asynchronously.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class OrderEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventProcessor.class);

    @Autowired
    private OrderReadModelRepository orderReadModelRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * Process order created event
     * 
     * @param event the order created event
     */
    @KafkaListener(topics = "order-events", groupId = "order-read-model-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("Processing order created event: {}", event);

        // Get user information to enrich the read model
        userServiceClient.getUserById(event.getUserId())
                .flatMap(user -> createOrderReadModel(event, user))
                .flatMap(orderReadModelRepository::save)
                .doOnSuccess(savedModel -> logger.info("Read model created for order: {}", savedModel.getId()))
                .doOnError(error -> logger.error("Error processing order created event: {}", error.getMessage()))
                .subscribe();
    }

    /**
     * Process order status updated event
     * 
     * @param event the status update event
     */
    @KafkaListener(topics = "order-events", groupId = "order-read-model-group")
    public void handleOrderStatusUpdatedEvent(OrderStatusUpdatedEvent event) {
        logger.info("Processing order status updated event: {}", event);

        orderReadModelRepository.findById(event.getOrderId())
                .map(readModel -> {
                    readModel.setStatus(com.bootcamp.order.model.OrderStatus.valueOf(event.getNewStatus()));
                    readModel.setUpdatedAt(LocalDateTime.now());
                    return readModel;
                })
                .flatMap(orderReadModelRepository::save)
                .doOnSuccess(updatedModel -> logger.info("Read model updated for order: {}", updatedModel.getId()))
                .doOnError(error -> logger.error("Error processing order status updated event: {}", error.getMessage()))
                .subscribe();
    }

    /**
     * Process order deleted event
     * 
     * @param event the order deleted event
     */
    @KafkaListener(topics = "order-events", groupId = "order-read-model-group")
    public void handleOrderDeletedEvent(OrderDeletedEvent event) {
        logger.info("Processing order deleted event: {}", event);

        orderReadModelRepository.findById(event.getOrderId())
                .map(readModel -> {
                    readModel.setIsDeleted(true);
                    readModel.setUpdatedAt(LocalDateTime.now());
                    return readModel;
                })
                .flatMap(orderReadModelRepository::save)
                .doOnSuccess(updatedModel -> logger.info("Read model marked as deleted for order: {}", updatedModel.getId()))
                .doOnError(error -> logger.error("Error processing order deleted event: {}", error.getMessage()))
                .subscribe();
    }

    /**
     * Create read model from order created event
     * 
     * @param event the order created event
     * @param user the user information
     * @return Mono with the created read model
     */
    private Mono<OrderReadModel> createOrderReadModel(OrderCreatedEvent event, UserDto user) {
        OrderReadModel readModel = new OrderReadModel(
                event.getOrderId(),
                event.getUserId(),
                event.getTotalAmount()
        );
        
        readModel.setShippingAddress(event.getShippingAddress());
        readModel.setNotes(event.getNotes());
        readModel.setCreatedAt(event.getTimestamp());
        readModel.setUpdatedAt(event.getTimestamp());
        readModel.setUserUsername(user.getUsername());
        readModel.setUserEmail(user.getEmail());
        readModel.setItemsCount(event.getItems().size());
        readModel.setIsDeleted(false);

        return Mono.just(readModel);
    }

    /**
     * Recovery method to rebuild the read model
     * in case of inconsistencies
     * 
     * @param orderId the order ID
     * @return Mono indicating completion
     */
    public Mono<Void> rebuildReadModel(Long orderId) {
        logger.info("Rebuilding read model for order: {}", orderId);
        
        // In a complete implementation, all events would be read
        // from the event store to rebuild the complete state
        // For simplicity, we just log the action
        
        return Mono.empty()
                .doOnSuccess(v -> logger.info("Read model rebuilt for order: {}", orderId))
                .doOnError(error -> logger.error("Error rebuilding read model: {}", error.getMessage()))
                .then();
    }
} 