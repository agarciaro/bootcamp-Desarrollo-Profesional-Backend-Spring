package com.bootcamp.notification.consumer;

import com.bootcamp.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * User Event Consumer
 * 
 * Consumes user events from Kafka and triggers appropriate notifications.
 * This demonstrates event-driven communication between microservices.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Consume user events from Kafka topic
     * 
     * @param userEvent the user event received from Kafka
     */
    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void consumeUserEvent(UserEvent userEvent) {
        logger.info("Received user event: {}", userEvent);

        try {
            switch (userEvent.getEventType()) {
                case "USER_CREATED":
                    handleUserCreated(userEvent);
                    break;
                case "USER_UPDATED":
                    handleUserUpdated(userEvent);
                    break;
                case "USER_DELETED":
                    handleUserDeleted(userEvent);
                    break;
                default:
                    logger.warn("Unknown user event type: {}", userEvent.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing user event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle user created event reactively
     * 
     * @param userEvent the user created event
     */
    private void handleUserCreated(UserEvent userEvent) {
        logger.info("Processing user created event for user: {}", userEvent.getUsername());
        
        // Send welcome notification reactively
        notificationService.sendWelcomeNotification(
            userEvent.getUserId(),
            userEvent.getUsername(),
            userEvent.getEmail()
        )
        .doOnSuccess(v -> logger.info("Welcome notification processed successfully for user: {}", userEvent.getUsername()))
        .doOnError(error -> logger.error("Failed to process welcome notification for user: {}", userEvent.getUsername(), error))
        .subscribe();
    }

    /**
     * Handle user updated event reactively
     * 
     * @param userEvent the user updated event
     */
    private void handleUserUpdated(UserEvent userEvent) {
        logger.info("Processing user updated event for user: {}", userEvent.getUsername());
        
        // Send profile update confirmation reactively
        notificationService.sendProfileUpdateNotification(
            userEvent.getUserId(),
            userEvent.getUsername(),
            userEvent.getEmail()
        )
        .doOnSuccess(v -> logger.info("Profile update notification processed successfully for user: {}", userEvent.getUsername()))
        .doOnError(error -> logger.error("Failed to process profile update notification for user: {}", userEvent.getUsername(), error))
        .subscribe();
    }

    /**
     * Handle user deleted event reactively
     * 
     * @param userEvent the user deleted event
     */
    private void handleUserDeleted(UserEvent userEvent) {
        logger.info("Processing user deleted event for user: {}", userEvent.getUsername());
        
        // Send account deletion confirmation reactively
        notificationService.sendAccountDeletionNotification(
            userEvent.getUserId(),
            userEvent.getUsername(),
            userEvent.getEmail()
        )
        .doOnSuccess(v -> logger.info("Account deletion notification processed successfully for user: {}", userEvent.getUsername()))
        .doOnError(error -> logger.error("Failed to process account deletion notification for user: {}", userEvent.getUsername(), error))
        .subscribe();
    }
}

 