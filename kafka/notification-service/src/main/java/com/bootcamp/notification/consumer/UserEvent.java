package com.bootcamp.notification.consumer;

/**
 * User Event class for Kafka consumption
 * 
 * Represents user events received from Kafka.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
public class UserEvent {
    private String eventType;
    private Long userId;
    private String username;
    private String email;
    private String timestamp;
    private String data;

    // Default constructor
    public UserEvent() {}

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
} 