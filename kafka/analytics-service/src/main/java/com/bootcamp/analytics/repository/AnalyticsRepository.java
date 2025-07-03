package com.bootcamp.analytics.repository;

import com.bootcamp.analytics.model.OrderMetrics;
import com.bootcamp.analytics.model.RevenueAnalytics;
import com.bootcamp.analytics.model.UserActivityMetrics;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Analytics Repository
 * 
 * Provides reactive database operations for analytics data.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Repository
public interface AnalyticsRepository extends ReactiveCrudRepository<OrderMetrics, Long> {

    /**
     * Save order metrics
     * 
     * @param metrics the order metrics to save
     * @return Mono with the saved metrics
     */
    Mono<OrderMetrics> saveOrderMetrics(OrderMetrics metrics);

    /**
     * Save user activity metrics
     * 
     * @param metrics the user activity metrics to save
     * @return Mono with the saved metrics
     */
    Mono<UserActivityMetrics> saveUserActivityMetrics(UserActivityMetrics metrics);

    /**
     * Save revenue analytics
     * 
     * @param analytics the revenue analytics to save
     * @return Mono with the saved analytics
     */
    Mono<RevenueAnalytics> saveRevenueAnalytics(RevenueAnalytics analytics);

    /**
     * Find order metrics by type
     * 
     * @param metricType the metric type
     * @return Flux containing order metrics
     */
    @Query("SELECT * FROM order_metrics WHERE metric_type = :metricType ORDER BY timestamp DESC")
    Flux<OrderMetrics> findOrderMetricsByType(String metricType);

    /**
     * Find user activity metrics by user ID
     * 
     * @param userId the user ID
     * @return Flux containing user activity metrics
     */
    @Query("SELECT * FROM user_activity_metrics WHERE user_id = :userId ORDER BY last_activity DESC")
    Flux<UserActivityMetrics> findUserActivityMetricsByUserId(Long userId);

    /**
     * Find revenue analytics by time period
     * 
     * @param timePeriod the time period
     * @return Flux containing revenue analytics
     */
    @Query("SELECT * FROM revenue_analytics WHERE time_period = :timePeriod ORDER BY created_at DESC")
    Flux<RevenueAnalytics> findRevenueAnalyticsByPeriod(String timePeriod);

    /**
     * Get total orders today
     * 
     * @return Mono with the total count
     */
    @Query("SELECT COUNT(*) FROM order_metrics WHERE DATE(timestamp) = CURDATE()")
    Mono<Long> getTotalOrdersToday();

    /**
     * Get total revenue today
     * 
     * @return Mono with the total revenue
     */
    @Query("SELECT COALESCE(SUM(metric_value), 0) FROM order_metrics WHERE DATE(timestamp) = CURDATE()")
    Mono<BigDecimal> getTotalRevenueToday();

    /**
     * Get active users today
     * 
     * @return Mono with the active user count
     */
    @Query("SELECT COUNT(DISTINCT user_id) FROM user_activity_metrics WHERE DATE(last_activity) = CURDATE()")
    Mono<Long> getActiveUsersToday();

    /**
     * Get average order value
     * 
     * @return Mono with the average order value
     */
    @Query("SELECT COALESCE(AVG(metric_value), 0) FROM order_metrics WHERE metric_type = 'ORDER_CREATED'")
    Mono<BigDecimal> getAverageOrderValue();

    /**
     * Find active alerts
     * 
     * @return Flux containing active alerts
     */
    @Query("SELECT * FROM alerts_history WHERE is_resolved = false ORDER BY triggered_at DESC")
    Flux<Map<String, Object>> findActiveAlerts();

    /**
     * Get order metrics by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return Flux containing order metrics
     */
    @Query("SELECT * FROM order_metrics WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    Flux<OrderMetrics> findOrderMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get revenue analytics by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return Flux containing revenue analytics
     */
    @Query("SELECT * FROM revenue_analytics WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    Flux<RevenueAnalytics> findRevenueAnalyticsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get top performing users by revenue
     * 
     * @param limit the number of users to return
     * @return Flux containing user revenue data
     */
    @Query("SELECT user_id, SUM(metric_value) as total_revenue FROM order_metrics " +
           "WHERE metric_type = 'ORDER_CREATED' GROUP BY user_id ORDER BY total_revenue DESC LIMIT :limit")
    Flux<Map<String, Object>> findTopPerformingUsers(int limit);

    /**
     * Get system health metrics
     * 
     * @return Flux containing system health metrics
     */
    @Query("SELECT * FROM system_health_metrics ORDER BY timestamp DESC LIMIT 100")
    Flux<Map<String, Object>> findSystemHealthMetrics();
} 