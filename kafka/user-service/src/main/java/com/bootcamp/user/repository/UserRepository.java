package com.bootcamp.user.repository;

import com.bootcamp.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * User Repository Interface
 * 
 * Provides reactive data access methods for User entities.
 * Extends ReactiveCrudRepository to get basic CRUD operations and adds custom query methods.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    /**
     * Find a user by their username
     * 
     * @param username the username to search for
     * @return Mono containing the user if found
     */
    Mono<User> findByUsername(String username);

    /**
     * Find a user by their email address
     * 
     * @param email the email to search for
     * @return Mono containing the user if found
     */
    Mono<User> findByEmail(String email);

    /**
     * Check if a username already exists
     * 
     * @param username the username to check
     * @return Mono<Boolean> true if username exists, false otherwise
     */
    Mono<Boolean> existsByUsername(String username);

    /**
     * Check if an email already exists
     * 
     * @param email the email to check
     * @return Mono<Boolean> true if email exists, false otherwise
     */
    Mono<Boolean> existsByEmail(String email);
} 