package com.bootcamp.analytics.service;

import com.bootcamp.analytics.model.OrderMetrics;
import com.bootcamp.analytics.model.RevenueAnalytics;
import com.bootcamp.analytics.model.UserActivityMetrics;
import com.bootcamp.analytics.repository.AnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Analytics Service
 * 
 * Provides business logic for analytics operations:
 * - Real-time metrics calculation and storage
 * - Analytics data retrieval and aggregation
 * - Performance monitoring and alerting
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Service
public class AnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @Autowired
    private AnalyticsRepository analyticsRepository;

    /**
     * Save order metrics reactively
     * 
     * @param metrics the order metrics to save
     * @return Mono with the saved metrics
     */
    public Mono<OrderMetrics> saveOrderMetrics(OrderMetrics metrics) {
        logger.info("Saving order metrics: {}", metrics);
        return analyticsRepository.saveOrderMetrics(metrics)
                .doOnSuccess(saved -> logger.info("Order metrics saved successfully: {}", saved.getId()));
    }

    /**
     * Save user activity metrics reactively
     * 
     * @param metrics the user activity metrics to save
     * @return Mono with the saved metrics
     */
    public Mono<UserActivityMetrics> saveUserActivityMetrics(UserActivityMetrics metrics) {
        logger.info("Saving user activity metrics: {}", metrics);
        return analyticsRepository.saveUserActivityMetrics(metrics)
                .doOnSuccess(saved -> logger.info("User activity metrics saved successfully: {}", saved.getId()));
    }

    /**
     * Save revenue analytics reactively
     * 
     * @param analytics the revenue analytics to save
     * @return Mono with the saved analytics
     */
    public Mono<RevenueAnalytics> saveRevenueAnalytics(RevenueAnalytics analytics) {
        logger.info("Saving revenue analytics: {}", analytics);
        return analyticsRepository.saveRevenueAnalytics(analytics)
                .doOnSuccess(saved -> logger.info("Revenue analytics saved successfully: {}", saved.getId()));
    }

    /**
     * Get real-time order metrics by type
     * 
     * @param metricType the type of metric
     * @return Flux containing order metrics
     */
    public Flux<OrderMetrics> getOrderMetricsByType(String metricType) {
        logger.info("Retrieving order metrics for type: {}", metricType);
        return analyticsRepository.findOrderMetricsByType(metricType)
                .doOnComplete(() -> logger.info("Retrieved order metrics for type: {}", metricType));
    }

    /**
     * Get user activity metrics by user ID
     * 
     * @param userId the user ID
     * @return Flux containing user activity metrics
     */
    public Flux<UserActivityMetrics> getUserActivityMetrics(Long userId) {
        logger.info("Retrieving user activity metrics for user: {}", userId);
        return analyticsRepository.findUserActivityMetricsByUserId(userId)
                .doOnComplete(() -> logger.info("Retrieved user activity metrics for user: {}", userId));
    }

    /**
     * Get revenue analytics by time period
     * 
     * @param timePeriod the time period (hourly, daily, weekly, monthly)
     * @return Flux containing revenue analytics
     */
    public Flux<RevenueAnalytics> getRevenueAnalyticsByPeriod(String timePeriod) {
        logger.info("Retrieving revenue analytics for period: {}", timePeriod);
        return analyticsRepository.findRevenueAnalyticsByPeriod(timePeriod)
                .doOnComplete(() -> logger.info("Retrieved revenue analytics for period: {}", timePeriod));
    }

    /**
     * Get real-time dashboard metrics
     * 
     * @return Mono with dashboard metrics
     */
    public Mono<Map<String, Object>> getDashboardMetrics() {
        logger.info("Retrieving dashboard metrics");
        
        return Mono.zip(
                analyticsRepository.getTotalOrdersToday(),
                analyticsRepository.getTotalRevenueToday(),
                analyticsRepository.getActiveUsersToday(),
                analyticsRepository.getAverageOrderValue()
        ).map(tuple -> {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("totalOrdersToday", tuple.getT1());
            metrics.put("totalRevenueToday", tuple.getT2());
            metrics.put("activeUsersToday", tuple.getT3());
            metrics.put("averageOrderValue", tuple.getT4());
            return metrics;
        }).doOnSuccess(metrics -> logger.info("Dashboard metrics retrieved: {}", metrics));
    }

    /**
     * Get real-time alerts
     * 
     * @return Flux containing active alerts
     */
    public Flux<Map<String, Object>> getActiveAlerts() {
        logger.info("Retrieving active alerts");
        return analyticsRepository.findActiveAlerts()
                .doOnComplete(() -> logger.info("Retrieved active alerts"));
    }

    /**
     * Process real-time analytics event
     * 
     * @param eventType the type of event
     * @param eventData the event data
     * @return Mono indicating completion
     */
    public Mono<Void> processAnalyticsEvent(String eventType, Map<String, Object> eventData) {
        logger.info("Processing analytics event: {} with data: {}", eventType, eventData);
        
        return Mono.fromRunnable(() -> {
            switch (eventType) {
                case "ORDER_CREATED":
                    processOrderCreatedEvent(eventData);
                    break;
                case "USER_ACTIVITY":
                    processUserActivityEvent(eventData);
                    break;
                case "REVENUE_UPDATE":
                    processRevenueUpdateEvent(eventData);
                    break;
                default:
                    logger.warn("Unknown analytics event type: {}", eventType);
            }
        }).then();
    }

    /**
     * Process order created event
     * 
     * @param eventData the event data
     */
    private void processOrderCreatedEvent(Map<String, Object> eventData) {
        try {
            OrderMetrics metrics = new OrderMetrics();
            metrics.setMetricType("ORDER_CREATED");
            metrics.setMetricValue(new BigDecimal(eventData.get("amount").toString()));
            metrics.setUserId(Long.valueOf(eventData.get("userId").toString()));
            metrics.setOrderId(Long.valueOf(eventData.get("orderId").toString()));
            metrics.setTimestamp(LocalDateTime.now());
            
            saveOrderMetrics(metrics).subscribe();
        } catch (Exception e) {
            logger.error("Error processing order created event: {}", e.getMessage(), e);
        }
    }

    /**
     * Process user activity event
     * 
     * @param eventData the event data
     */
    private void processUserActivityEvent(Map<String, Object> eventData) {
        try {
            UserActivityMetrics metrics = new UserActivityMetrics();
            metrics.setUserId(Long.valueOf(eventData.get("userId").toString()));
            metrics.setActivityType(eventData.get("activityType").toString());
            metrics.setActivityCount(Long.valueOf(eventData.get("count").toString()));
            metrics.setLastActivity(LocalDateTime.now());
            
            saveUserActivityMetrics(metrics).subscribe();
        } catch (Exception e) {
            logger.error("Error processing user activity event: {}", e.getMessage(), e);
        }
    }

    /**
     * Process revenue update event
     * 
     * @param eventData the event data
     */
    private void processRevenueUpdateEvent(Map<String, Object> eventData) {
        try {
            RevenueAnalytics analytics = new RevenueAnalytics();
            analytics.setRevenueAmount(new BigDecimal(eventData.get("amount").toString()));
            analytics.setOrderCount(Long.valueOf(eventData.get("orderCount").toString()));
            analytics.setTimePeriod(eventData.get("timePeriod").toString());
            analytics.setPeriodStart(LocalDateTime.now());
            analytics.setPeriodEnd(LocalDateTime.now().plusHours(1));
            
            saveRevenueAnalytics(analytics).subscribe();
        } catch (Exception e) {
            logger.error("Error processing revenue update event: {}", e.getMessage(), e);
        }
    }
} 