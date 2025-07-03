package com.bootcamp.analytics.consumer;

import com.bootcamp.analytics.service.AnalyticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Analytics Event Consumer
 * 
 * Consumes analytics events from Kafka topics and processes them
 * for real-time analytics and metrics calculation.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class AnalyticsEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsEventConsumer.class);

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Consume analytics events from Kafka topic
     * 
     * @param eventJson the analytics event as JSON string
     */
    @KafkaListener(topics = "analytics-events", groupId = "analytics-service-group")
    public void consumeAnalyticsEvent(String eventJson) {
        logger.info("Received analytics event: {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            String eventType = (String) event.get("type");
            
            analyticsService.processAnalyticsEvent(eventType, event)
                .doOnSuccess(unused -> logger.info("Analytics event processed successfully: {}", eventType))
                .doOnError(error -> logger.error("Error processing analytics event: {}", error.getMessage()))
                .subscribe();
        } catch (JsonProcessingException e) {
            logger.error("Error parsing analytics event: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing analytics event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume revenue analytics events from Kafka topic
     * 
     * @param eventJson the revenue analytics event as JSON string
     */
    @KafkaListener(topics = "revenue-analytics", groupId = "analytics-service-group")
    public void consumeRevenueAnalyticsEvent(String eventJson) {
        logger.info("Received revenue analytics event: {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            
            analyticsService.processAnalyticsEvent("REVENUE_UPDATE", event)
                .doOnSuccess(unused -> logger.info("Revenue analytics event processed successfully"))
                .doOnError(error -> logger.error("Error processing revenue analytics event: {}", error.getMessage()))
                .subscribe();
        } catch (JsonProcessingException e) {
            logger.error("Error parsing revenue analytics event: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing revenue analytics event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume user activity analytics events from Kafka topic
     * 
     * @param eventJson the user activity analytics event as JSON string
     */
    @KafkaListener(topics = "user-activity-analytics", groupId = "analytics-service-group")
    public void consumeUserActivityAnalyticsEvent(String eventJson) {
        logger.info("Received user activity analytics event: {}", eventJson);

        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            
            analyticsService.processAnalyticsEvent("USER_ACTIVITY", event)
                .doOnSuccess(unused -> logger.info("User activity analytics event processed successfully"))
                .doOnError(error -> logger.error("Error processing user activity analytics event: {}", error.getMessage()))
                .subscribe();
        } catch (JsonProcessingException e) {
            logger.error("Error parsing user activity analytics event: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing user activity analytics event: {}", e.getMessage(), e);
        }
    }

    /**
     * Consume analytics alerts from Kafka topic
     * 
     * @param alertJson the analytics alert as JSON string
     */
    @KafkaListener(topics = "analytics-alerts", groupId = "analytics-service-group")
    public void consumeAnalyticsAlert(String alertJson) {
        logger.info("Received analytics alert: {}", alertJson);

        try {
            Map<String, Object> alert = objectMapper.readValue(alertJson, Map.class);
            String alertType = (String) alert.get("type");
            String message = (String) alert.get("message");
            
            logger.warn("Analytics Alert - Type: {}, Message: {}", alertType, message);
            
            // TODO: Implement alert processing logic
            // - Store alert in database
            // - Send notifications
            // - Trigger automated responses
            
        } catch (JsonProcessingException e) {
            logger.error("Error parsing analytics alert: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error processing analytics alert: {}", e.getMessage(), e);
        }
    }
} 