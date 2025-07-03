package com.bootcamp.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order Metrics Model
 * 
 * Represents order-related metrics for analytics.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Table("order_metrics")
public class OrderMetrics {
    
    @Id
    private Long id;
    private String metricType;
    private BigDecimal metricValue;
    private Long userId;
    private Long orderId;
    private LocalDateTime timestamp;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;

    // Default constructor
    public OrderMetrics() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with parameters
    public OrderMetrics(String metricType, BigDecimal metricValue, Long userId, Long orderId) {
        this();
        this.metricType = metricType;
        this.metricValue = metricValue;
        this.userId = userId;
        this.orderId = orderId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
        return "OrderMetrics{" +
                "id=" + id +
                ", metricType='" + metricType + '\'' +
                ", metricValue=" + metricValue +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", timestamp=" + timestamp +
                '}';
    }
} 