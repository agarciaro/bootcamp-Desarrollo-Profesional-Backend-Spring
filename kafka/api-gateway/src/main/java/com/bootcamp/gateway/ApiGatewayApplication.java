package com.bootcamp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application
 * 
 * This service acts as the entry point for all client requests.
 * It routes requests to the appropriate microservices based on URL patterns.
 * Provides cross-cutting concerns like authentication, rate limiting, and logging.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
} 