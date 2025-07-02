package com.bootcamp.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Notification Service
 * 
 * Contains business logic for notification operations.
 * Handles sending different types of notifications to users.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Send welcome notification to new user reactively
     * 
     * @param userId the user ID
     * @param username the username
     * @param email the user email
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> sendWelcomeNotification(Long userId, String username, String email) {
        logger.info("Sending welcome notification to user: {} (ID: {})", username, userId);
        
        String message = String.format(
            "Welcome %s! Your account has been successfully created. " +
            "You can now start using our services.", username
        );
        
        return simulateNotificationSending(email, "Welcome to Our Platform", message)
                .doOnSuccess(v -> logger.info("Welcome notification sent successfully to user: {}", username))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Send profile update notification reactively
     * 
     * @param userId the user ID
     * @param username the username
     * @param email the user email
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> sendProfileUpdateNotification(Long userId, String username, String email) {
        logger.info("Sending profile update notification to user: {} (ID: {})", username, userId);
        
        String message = String.format(
            "Hello %s! Your profile has been successfully updated. " +
            "If you didn't make this change, please contact support immediately.", username
        );
        
        return simulateNotificationSending(email, "Profile Updated", message)
                .doOnSuccess(v -> logger.info("Profile update notification sent successfully to user: {}", username))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Send account deletion notification reactively
     * 
     * @param userId the user ID
     * @param username the username
     * @param email the user email
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> sendAccountDeletionNotification(Long userId, String username, String email) {
        logger.info("Sending account deletion notification to user: {} (ID: {})", username, userId);
        
        String message = String.format(
            "Hello %s! Your account has been successfully deleted. " +
            "We're sorry to see you go. You can always create a new account if you change your mind.", username
        );
        
        return simulateNotificationSending(email, "Account Deleted", message)
                .doOnSuccess(v -> logger.info("Account deletion notification sent successfully to user: {}", username))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Send order confirmation notification reactively
     * 
     * @param userId the user ID
     * @param username the username
     * @param email the user email
     * @param orderId the order ID
     * @param totalAmount the order total amount
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> sendOrderConfirmationNotification(Long userId, String username, String email, 
                                                       Long orderId, String totalAmount) {
        logger.info("Sending order confirmation notification to user: {} for order: {}", username, orderId);
        
        String message = String.format(
            "Hello %s! Your order #%d has been confirmed. " +
            "Total amount: %s. You will receive shipping updates soon.", 
            username, orderId, totalAmount
        );
        
        return simulateNotificationSending(email, "Order Confirmed", message)
                .doOnSuccess(v -> logger.info("Order confirmation notification sent successfully to user: {}", username))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Send order status update notification reactively
     * 
     * @param userId the user ID
     * @param username the username
     * @param email the user email
     * @param orderId the order ID
     * @param status the new order status
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> sendOrderStatusUpdateNotification(Long userId, String username, String email, 
                                                       Long orderId, String status) {
        logger.info("Sending order status update notification to user: {} for order: {}", username, orderId);
        
        String message = String.format(
            "Hello %s! Your order #%d status has been updated to: %s. " +
            "Thank you for your patience.", username, orderId, status
        );
        
        return simulateNotificationSending(email, "Order Status Updated", message)
                .doOnSuccess(v -> logger.info("Order status update notification sent successfully to user: {}", username))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Simulate sending a notification reactively
     * 
     * In a real application, this would integrate with:
     * - Email service (SendGrid, AWS SES, etc.)
     * - SMS service (Twilio, AWS SNS, etc.)
     * - Push notification service (Firebase, etc.)
     * - In-app notification system
     * 
     * @param recipient the recipient email/phone
     * @param subject the notification subject
     * @param message the notification message
     * @return Mono<Void> indicating completion
     */
    private Mono<Void> simulateNotificationSending(String recipient, String subject, String message) {
        return Mono.fromRunnable(() -> {
            try {
                // Simulate network delay
                Thread.sleep(100);
                
                logger.debug("Notification sent - To: {}, Subject: {}, Message: {}", 
                            recipient, subject, message);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Notification sending interrupted: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Failed to send notification: {}", e.getMessage(), e);
            }
        })
        .then()
        .subscribeOn(Schedulers.boundedElastic());
    }
} 