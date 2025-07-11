package com.bootcamp.user.service;

import com.bootcamp.user.dto.UserDto;
import com.bootcamp.user.event.UserEvent;
import com.bootcamp.user.event.UserEventType;
import com.bootcamp.user.model.User;
import com.bootcamp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * User Service
 * 
 * Contains business logic for user management operations.
 * Handles user CRUD operations and publishes events to Kafka for other services.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String USER_EVENTS_TOPIC = "user-events";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    /**
     * Create a new user reactively
     * 
     * @param userDto the user data to create
     * @return Mono containing the created user DTO
     */
    public Mono<UserDto> createUser(UserDto userDto) {
        logger.info("Creating new user with username: {}", userDto.getUsername());

        return checkUserExists(userDto.getUsername(), userDto.getEmail())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new RuntimeException("Username or email already exists"));
                }
                return Mono.just(userDto);
            })
            .map(dto -> new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName()
            ))
            .flatMap(userRepository::save)
            .doOnNext(savedUser -> {
                logger.info("User created successfully with ID: {}", savedUser.getId());
                publishUserEvent(UserEventType.USER_CREATED.toString(), savedUser);
            })
            .map(this::convertToDto);
    }

    /**
     * Get all users reactively
     * 
     * @return Flux containing all user DTOs
     */
    public Flux<UserDto> getAllUsers() {
        logger.info("Retrieving all users");
        return userRepository.findAll()
                .map(this::convertToDto);
    }

    /**
     * Get user by ID reactively
     * 
     * @param id the user ID
     * @return Mono containing the user DTO if found
     */
    public Mono<UserDto> getUserById(Long id) {
        logger.info("Retrieving user with ID: {}", id);
        return userRepository.findById(id)
                .map(this::convertToDto)
                .switchIfEmpty(Mono.empty());
    }

    /**
     * Get user by username reactively
     * 
     * @param username the username to search for
     * @return Mono containing the user DTO if found
     */
    public Mono<UserDto> getUserByUsername(String username) {
        logger.info("Retrieving user with username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .switchIfEmpty(Mono.empty());
    }

    /**
     * Update an existing user reactively
     * 
     * @param id the user ID to update
     * @param userDto the updated user data
     * @return Mono containing the updated user DTO
     */
    public Mono<UserDto> updateUser(Long id, UserDto userDto) {
        logger.info("Updating user with ID: {}", id);

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with ID: " + id)))
                .map(user -> {
                    // Update user fields
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setEmail(userDto.getEmail());
                    user.preUpdate();
                    return user;
                })
                .flatMap(userRepository::save)
                .doOnNext(updatedUser -> {
                    logger.info("User updated successfully with ID: {}", updatedUser.getId());
                    publishUserEvent(UserEventType.USER_UPDATED.toString(), updatedUser);
                })
                .map(this::convertToDto);
    }

    /**
     * Delete a user reactively
     * 
     * @param id the user ID to delete
     * @return Mono<Void> indicating completion
     */
    public Mono<Void> deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with ID: " + id)))
                .doOnNext(user -> {
                    // Publish user deleted event to Kafka before deletion
                    publishUserEvent(UserEventType.USER_DELETED.toString(), user);
                })
                .then(userRepository.deleteById(id))
                .doOnSuccess(v -> logger.info("User deleted successfully with ID: {}", id));
    }

    /**
     * Convert User entity to UserDto
     * 
     * @param user the user entity
     * @return the user DTO
     */
    private UserDto convertToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getStatus().toString()
        );
    }

    /**
     * Check if user exists by username or email
     * 
     * @param username the username to check
     * @param email the email to check
     * @return Mono<Boolean> true if user exists, false otherwise
     */
    private Mono<Boolean> checkUserExists(String username, String email) {
        return Mono.zip(
            userRepository.existsByUsername(username),
            userRepository.existsByEmail(email)
        ).map(tuple -> tuple.getT1() || tuple.getT2());
    }

    /**
     * Publish user event to Kafka
     * 
     * @param eventType the type of event
     * @param user the user associated with the event
     */
    private void publishUserEvent(String eventType, User user) {
        try {
            UserEvent event = new UserEvent(eventType, user.getId(), user.getUsername(), user.getEmail());
            kafkaTemplate.send(USER_EVENTS_TOPIC, event);
            logger.info("Published {} event for user: {}", eventType, user.getUsername());
        } catch (Exception e) {
            logger.error("Failed to publish user event: {}", e.getMessage(), e);
            // In a production environment, you might want to handle this differently
            // For example, store failed events in a database for retry
        }
    }
} 