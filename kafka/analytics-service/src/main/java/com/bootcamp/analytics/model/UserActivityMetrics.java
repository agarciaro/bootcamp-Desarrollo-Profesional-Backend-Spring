package com.bootcamp.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * User Activity Metrics Model
 * 
 * Represents user activity metrics for analytics.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Table("user_activity_metrics")
public class UserActivityMetrics {
    
    @Id
    private Long id;
    private Long userId;
    private String activityType;
    private Long activityCount;
    private LocalDateTime lastActivity;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;

    // Default constructor
    public UserActivityMetrics() {
        this.lastActivity = LocalDateTime.now();
        this.activityCount = 0L;
    }

    // Constructor with parameters
    public UserActivityMetrics(Long userId, String activityType, Long activityCount) {
        this();
        this.userId = userId;
        this.activityType = activityType;
        this.activityCount = activityCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Long getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(Long activityCount) {
        this.activityCount = activityCount;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public LocalDateTime getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(LocalDateTime windowStart) {
        this.windowStart = windowStart;
    }

    public LocalDateTime getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(LocalDateTime windowEnd) {
        this.windowEnd = windowEnd;
    }

    @Override
    public String toString() {
        return "UserActivityMetrics{" +
                "id=" + id +
                ", userId=" + userId +
                ", activityType='" + activityType + '\'' +
                ", activityCount=" + activityCount +
                ", lastActivity=" + lastActivity +
                '}';
    }
} 