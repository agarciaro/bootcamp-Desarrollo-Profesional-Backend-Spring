package com.bootcamp.user.controller;

import com.bootcamp.user.dto.UserDto;
import com.bootcamp.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * User Controller
 * 
 * REST API endpoints for user management operations.
 * Provides CRUD operations for users and handles HTTP requests/responses.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Create a new user reactively
     * 
     * @param userDto the user data to create
     * @return ResponseEntity with the created user
     */
    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        logger.info("Received request to create user: {}", userDto.getUsername());
        
        return userService.createUser(userDto)
                .map(createdUser -> {
                    logger.info("User created successfully with ID: {}", createdUser.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
                })
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to create user: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().<UserDto>build());
                });
    }

    /**
     * Get all users reactively
     * 
     * @return ResponseEntity with Flux of all users
     */
    @GetMapping
    public Flux<UserDto> getAllUsers() {
        logger.info("Received request to get all users");
        
        return userService.getAllUsers()
                .doOnComplete(() -> logger.info("Retrieved all users"));
    }

    /**
     * Get user by ID reactively
     * 
     * @param id the user ID
     * @return ResponseEntity with the user if found
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id) {
        logger.info("Received request to get user with ID: {}", id);
        
        return userService.getUserById(id)
                .map(user -> {
                    logger.info("User found with ID: {}", id);
                    return ResponseEntity.ok(user);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(response -> {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        logger.warn("User not found with ID: {}", id);
                    }
                });
    }

    /**
     * Get user by username reactively
     * 
     * @param username the username to search for
     * @return ResponseEntity with the user if found
     */
    @GetMapping("/username/{username}")
    public Mono<ResponseEntity<UserDto>> getUserByUsername(@PathVariable String username) {
        logger.info("Received request to get user with username: {}", username);
        
        return userService.getUserByUsername(username)
                .map(user -> {
                    logger.info("User found with username: {}", username);
                    return ResponseEntity.ok(user);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(response -> {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        logger.warn("User not found with username: {}", username);
                    }
                });
    }

    /**
     * Update an existing user reactively
     * 
     * @param id the user ID to update
     * @param userDto the updated user data
     * @return ResponseEntity with the updated user
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        logger.info("Received request to update user with ID: {}", id);
        
        return userService.updateUser(id, userDto)
                .map(updatedUser -> {
                    logger.info("User updated successfully with ID: {}", id);
                    return ResponseEntity.ok(updatedUser);
                })
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to update user: {}", e.getMessage());
                    return Mono.just(ResponseEntity.notFound().<UserDto>build());
                });
    }

    /**
     * Delete a user reactively
     * 
     * @param id the user ID to delete
     * @return ResponseEntity with no content if successful
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with ID: {}", id);
        
        return userService.deleteUser(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .doOnNext(response -> logger.info("User deleted successfully with ID: {}", id))
                .onErrorResume(RuntimeException.class, e -> {
                    logger.error("Failed to delete user: {}", e.getMessage());
                    return Mono.just(ResponseEntity.notFound().<Void>build());
                });
    }

    /**
     * Health check endpoint reactively
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        logger.debug("Health check requested");
        return Mono.just(ResponseEntity.ok("User Service is running"));
    }
} 