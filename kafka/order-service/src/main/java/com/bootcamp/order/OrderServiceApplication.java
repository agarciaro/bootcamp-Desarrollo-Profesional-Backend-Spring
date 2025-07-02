package com.bootcamp.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Order Service Application
 * 
 * This microservice handles order management operations including:
 * - Order creation and processing
 * - Order status management
 * - Integration with User Service via REST calls
 * - Publishing order events to Kafka
 * - Consuming user events from Kafka
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
} 