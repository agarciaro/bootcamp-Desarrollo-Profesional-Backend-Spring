package com.bootcamp.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Revenue Analytics Model
 * 
 * Represents revenue analytics data for reporting.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Table("revenue_analytics")
public class RevenueAnalytics {
    
    @Id
    private Long id;
    private BigDecimal revenueAmount;
    private Long orderCount;
    private Long userId;
    private String timePeriod;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime createdAt;

    // Default constructor
    public RevenueAnalytics() {
        this.createdAt = LocalDateTime.now();
        this.orderCount = 0L;
    }

    // Constructor with parameters
    public RevenueAnalytics(BigDecimal revenueAmount, Long orderCount, String timePeriod) {
        this();
        this.revenueAmount = revenueAmount;
        this.orderCount = orderCount;
        this.timePeriod = timePeriod;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(BigDecimal revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RevenueAnalytics{" +
                "id=" + id +
                ", revenueAmount=" + revenueAmount +
                ", orderCount=" + orderCount +
                ", userId=" + userId +
                ", timePeriod='" + timePeriod + '\'' +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", createdAt=" + createdAt +
                '}';
    }
} 