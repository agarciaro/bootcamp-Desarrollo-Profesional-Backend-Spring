package com.bootcamp.analytics.controller;

import com.bootcamp.analytics.model.OrderMetrics;
import com.bootcamp.analytics.model.RevenueAnalytics;
import com.bootcamp.analytics.model.UserActivityMetrics;
import com.bootcamp.analytics.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Analytics Controller
 * 
 * Provides REST endpoints for real-time analytics data:
 * - Dashboard metrics
 * - Order analytics
 * - User activity analytics
 * - Revenue analytics
 * - System health monitoring
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Get real-time dashboard metrics
     * 
     * @return ResponseEntity with dashboard metrics
     */
    @GetMapping("/dashboard")
    public Mono<ResponseEntity<Map<String, Object>>> getDashboardMetrics() {
        logger.info("Received request for dashboard metrics");
        
        return analyticsService.getDashboardMetrics()
                .map(metrics -> {
                    logger.info("Dashboard metrics retrieved: {}", metrics);
                    return ResponseEntity.ok(metrics);
                })
                .onErrorResume(e -> {
                    logger.error("Error retrieving dashboard metrics: {}", e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    /**
     * Get order metrics by type
     * 
     * @param metricType the metric type
     * @return Flux containing order metrics
     */
    @GetMapping("/orders/metrics/{metricType}")
    public Flux<OrderMetrics> getOrderMetricsByType(@PathVariable String metricType) {
        logger.info("Received request for order metrics with type: {}", metricType);
        return analyticsService.getOrderMetricsByType(metricType)
                .doOnComplete(() -> logger.info("Retrieved order metrics for type: {}", metricType));
    }

    /**
     * Get user activity metrics by user ID
     * 
     * @param userId the user ID
     * @return Flux containing user activity metrics
     */
    @GetMapping("/users/{userId}/activity")
    public Flux<UserActivityMetrics> getUserActivityMetrics(@PathVariable Long userId) {
        logger.info("Received request for user activity metrics for user: {}", userId);
        return analyticsService.getUserActivityMetrics(userId)
                .doOnComplete(() -> logger.info("Retrieved user activity metrics for user: {}", userId));
    }

    /**
     * Get revenue analytics by time period
     * 
     * @param timePeriod the time period (hourly, daily, weekly, monthly)
     * @return Flux containing revenue analytics
     */
    @GetMapping("/revenue/{timePeriod}")
    public Flux<RevenueAnalytics> getRevenueAnalyticsByPeriod(@PathVariable String timePeriod) {
        logger.info("Received request for revenue analytics for period: {}", timePeriod);
        return analyticsService.getRevenueAnalyticsByPeriod(timePeriod)
                .doOnComplete(() -> logger.info("Retrieved revenue analytics for period: {}", timePeriod));
    }

    /**
     * Get active alerts
     * 
     * @return Flux containing active alerts
     */
    @GetMapping("/alerts")
    public Flux<Map<String, Object>> getActiveAlerts() {
        logger.info("Received request for active alerts");
        return analyticsService.getActiveAlerts()
                .doOnComplete(() -> logger.info("Retrieved active alerts"));
    }

    /**
     * Get real-time analytics stream
     * 
     * @return Flux with real-time analytics events
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> getAnalyticsStream() {
        logger.info("Received request for analytics stream");
        
        return Flux.interval(java.time.Duration.ofSeconds(5))
                .flatMap(tick -> analyticsService.getDashboardMetrics())
                .map(metrics -> "data: " + metrics.toString() + "\n\n")
                .doOnComplete(() -> logger.info("Analytics stream completed"));
    }

    /**
     * Get system health metrics
     * 
     * @return Flux containing system health metrics
     */
    @GetMapping("/health/metrics")
    public Flux<Map<String, Object>> getSystemHealthMetrics() {
        logger.info("Received request for system health metrics");
        return Flux.empty(); // TODO: Implement system health metrics
    }

    /**
     * Get top performing users
     * 
     * @param limit the number of users to return (default: 10)
     * @return Flux containing top performing users
     */
    @GetMapping("/users/top-performing")
    public Flux<Map<String, Object>> getTopPerformingUsers(
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("Received request for top performing users with limit: {}", limit);
        return Flux.empty(); // TODO: Implement top performing users
    }

    /**
     * Get analytics summary for date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return Mono with analytics summary
     */
    @GetMapping("/summary")
    public Mono<ResponseEntity<Map<String, Object>>> getAnalyticsSummary(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        logger.info("Received request for analytics summary from {} to {}", startDate, endDate);
        
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("startDate", start);
            summary.put("endDate", end);
            summary.put("message", "Analytics summary endpoint - implementation pending");
            
            return Mono.just(summary).map(s -> {
                logger.info("Analytics summary retrieved for date range");
                return ResponseEntity.ok(s);
            });
        } catch (Exception e) {
            logger.error("Error parsing date range: {}", e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    /**
     * Health check endpoint
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        logger.debug("Health check requested for analytics service");
        return Mono.just(ResponseEntity.ok("Analytics Service is running"));
    }

    /**
     * Get Kafka Streams status
     * 
     * @return Mono with streams status
     */
    @GetMapping("/streams/status")
    public Mono<ResponseEntity<Map<String, Object>>> getStreamsStatus() {
        logger.info("Received request for Kafka Streams status");
        
        Map<String, Object> status = new HashMap<>();
        status.put("status", "running");
        status.put("applicationId", "analytics-streams-app");
        status.put("topics", new String[]{"order-events", "user-events", "analytics-events", "revenue-analytics", "user-activity-analytics", "analytics-alerts"});
        status.put("timestamp", LocalDateTime.now());
        
        return Mono.just(status).map(s -> {
            logger.info("Kafka Streams status retrieved: {}", s);
            return ResponseEntity.ok(s);
        });
    }
} 