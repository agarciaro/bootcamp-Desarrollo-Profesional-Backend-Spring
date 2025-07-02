package com.bootcamp.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Order Service Application
 * 
 * This microservice handles order management operations including:
 * - Order creation and processing
 * - Order status management
 * - Integration with User Service via WebClient
 * - Publishing order events to Kafka
 * - Consuming user events from Kafka
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
} 