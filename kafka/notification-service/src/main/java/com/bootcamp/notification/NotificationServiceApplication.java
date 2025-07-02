package com.bootcamp.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Notification Service Application
 * 
 * This microservice handles notification operations including:
 * - Consuming events from Kafka (user events, order events)
 * - Sending notifications to users
 * - Storing notification history
 * - Managing notification preferences
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
} 