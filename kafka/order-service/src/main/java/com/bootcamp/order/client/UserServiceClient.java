package com.bootcamp.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User Service Client
 * 
 * Feign client for making HTTP calls to the User Service.
 * This enables service-to-service communication between Order Service and User Service.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {

    /**
     * Get user by ID
     * 
     * @param id the user ID
     * @return UserDto containing user information
     */
    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    /**
     * Get user by username
     * 
     * @param username the username
     * @return UserDto containing user information
     */
    @GetMapping("/users/username/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);

    /**
     * Health check endpoint
     * 
     * @return health status string
     */
    @GetMapping("/users/health")
    String health();
}

 