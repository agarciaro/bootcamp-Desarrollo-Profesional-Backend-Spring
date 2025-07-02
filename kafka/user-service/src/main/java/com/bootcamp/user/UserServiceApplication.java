package com.bootcamp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * User Service Application
 * 
 * This microservice handles user management operations including:
 * - User registration and authentication
 * - User profile management
 * - User data persistence
 * - Publishing user events to Kafka for other services
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
} 