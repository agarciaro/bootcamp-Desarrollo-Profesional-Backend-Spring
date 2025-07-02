package com.bootcamp.user.event;

import java.time.LocalDateTime;

/**
 * User Event
 * 
 * Represents events related to user operations that will be published to Kafka.
 * Other microservices can consume these events to react to user changes.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class UserEvent {

    private String eventType;
    private Long userId;
    private String username;
    private String email;
    private LocalDateTime timestamp;
    private String data;

    // Default constructor
    public UserEvent() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with parameters
    public UserEvent(String eventType, Long userId, String username, String email) {
        this();
        this.eventType = eventType;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    // Constructor with all fields
    public UserEvent(String eventType, Long userId, String username, String email, String data) {
        this(eventType, userId, username, email);
        this.data = data;
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType='" + eventType + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                '}';
    }
}

/**
 * User Event Types
 * 
 * Defines the different types of user events that can be published.
 */
class UserEventType {
    public static final String USER_CREATED = "USER_CREATED";
    public static final String USER_UPDATED = "USER_UPDATED";
    public static final String USER_DELETED = "USER_DELETED";
    public static final String USER_ACTIVATED = "USER_ACTIVATED";
    public static final String USER_DEACTIVATED = "USER_DEACTIVATED";
} 