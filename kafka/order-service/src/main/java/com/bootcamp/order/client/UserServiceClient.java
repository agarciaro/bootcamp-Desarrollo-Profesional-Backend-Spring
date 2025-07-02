package com.bootcamp.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * User Service Client
 * 
 * WebClient for making reactive HTTP calls to the User Service.
 * This enables service-to-service communication between Order Service and User Service.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    private static final String USER_SERVICE_NAME = "user-service";

    @Autowired
    @LoadBalanced
    private WebClient.Builder webClientBuilder;

    /**
     * Get user by ID reactively
     * 
     * @param id the user ID
     * @return Mono containing UserDto with user information
     */
    public Mono<UserDto> getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        
        return webClientBuilder
            .baseUrl(getUserServiceUrl())
            .build()
            .get()
            .uri("/users/{id}", id)
            .retrieve()
            .bodyToMono(UserDto.class)
            .doOnSuccess(user -> logger.debug("Successfully fetched user: {}", user.getUsername()))
            .doOnError(error -> logger.error("Failed to fetch user with ID {}: {}", id, error.getMessage()));
    }

    /**
     * Get user by username reactively
     * 
     * @param username the username
     * @return Mono containing UserDto with user information
     */
    public Mono<UserDto> getUserByUsername(String username) {
        logger.debug("Fetching user with username: {}", username);
        
        return webClientBuilder
            .baseUrl(getUserServiceUrl())
            .build()
            .get()
            .uri("/users/username/{username}", username)
            .retrieve()
            .bodyToMono(UserDto.class)
            .doOnSuccess(user -> logger.debug("Successfully fetched user: {}", user.getUsername()))
            .doOnError(error -> logger.error("Failed to fetch user with username {}: {}", username, error.getMessage()));
    }

    /**
     * Health check endpoint reactively
     * 
     * @return Mono containing health status string
     */
    public Mono<String> health() {
        logger.debug("Checking user service health");
        
        return webClientBuilder
            .baseUrl(getUserServiceUrl())
            .build()
            .get()
            .uri("/users/health")
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(health -> logger.debug("User service health: {}", health))
            .doOnError(error -> logger.error("Failed to check user service health: {}", error.getMessage()));
    }

    /**
     * Get the base URL for the user service using service discovery
     * 
     * @return the base URL for the user service
     */
    private String getUserServiceUrl() {
        return "http://" + USER_SERVICE_NAME;
    }
}

 