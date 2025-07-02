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

/**
 * User DTO for Feign client
 * 
 * Simplified DTO for user information needed by Order Service.
 */
class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String status;

    // Default constructor
    public UserDto() {}

    // Constructor with parameters
    public UserDto(Long id, String username, String email, String firstName, String lastName, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
} 