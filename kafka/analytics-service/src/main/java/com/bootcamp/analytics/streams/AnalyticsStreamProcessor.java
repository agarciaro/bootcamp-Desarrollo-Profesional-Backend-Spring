package com.bootcamp.analytics.streams;

import com.bootcamp.analytics.model.OrderEvent;
import com.bootcamp.analytics.model.UserEvent;
import com.bootcamp.analytics.service.AnalyticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Analytics Stream Processor
 * 
 * Processes Kafka streams for real-time analytics:
 * - Order events processing and aggregation
 * - User events processing and activity tracking
 * - Real-time metrics calculation
 * - Revenue analytics and trending
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class AnalyticsStreamProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsStreamProcessor.class);
    
    // Topic names
    private static final String ORDER_EVENTS_TOPIC = "order-events";
    private static final String USER_EVENTS_TOPIC = "user-events";
    private static final String ANALYTICS_TOPIC = "analytics-events";
    private static final String REVENUE_ANALYTICS_TOPIC = "revenue-analytics";
    private static final String USER_ACTIVITY_TOPIC = "user-activity-analytics";
    private static final String ALERTS_TOPIC = "analytics-alerts";

    @Autowired
    private StreamsBuilder streamsBuilder;

    @Autowired
    private Serde<OrderEvent> orderEventSerde;

    @Autowired
    private Serde<UserEvent> userEventSerde;

    @Autowired
    private Serde<String> stringSerde;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Build the analytics stream processing topology
     */
    public void buildTopology() {
        logger.info("Building analytics stream processing topology");

        // Process order events
        processOrderEvents();

        // Process user events
        processUserEvents();

        // Cross-stream analytics
        processCrossStreamAnalytics();

        logger.info("Analytics stream processing topology built successfully");
    }

    /**
     * Process order events for real-time analytics
     */
    private void processOrderEvents() {
        // Create KStream from order events topic
        KStream<String, String> orderStream = streamsBuilder.stream(ORDER_EVENTS_TOPIC);

        // Parse order events and filter valid ones
        KStream<String, OrderEvent> parsedOrderStream = orderStream
                .mapValues(this::parseOrderEvent)
                .filter((key, value) -> value != null);

        // Real-time order metrics by status
        parsedOrderStream
                .filter((key, order) -> "ORDER_CREATED".equals(order.getEventType()))
                .groupBy((key, order) -> order.getStatus())
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .map((key, value) -> KeyValue.pair(
                        key.key() + "-" + key.window().startTime().toString(),
                        "Order count for status " + key.key() + ": " + value
                ))
                .to(ANALYTICS_TOPIC);

        // Real-time revenue analytics
        parsedOrderStream
                .filter((key, order) -> "ORDER_CREATED".equals(order.getEventType()))
                .groupBy((key, order) -> "revenue")
                .windowedBy(TimeWindows.of(Duration.ofMinutes(10)))
                .aggregate(
                        () -> BigDecimal.ZERO,
                        (key, order, total) -> total.add(order.getTotalAmount()),
                        Materialized.with(Serdes.String(), new BigDecimalSerde())
                )
                .toStream()
                .map((key, value) -> KeyValue.pair(
                        "revenue-" + key.window().startTime().toString(),
                        "Total revenue: $" + value
                ))
                .to(REVENUE_ANALYTICS_TOPIC);

        // High-value order alerts
        parsedOrderStream
                .filter((key, order) -> "ORDER_CREATED".equals(order.getEventType()))
                .filter((key, order) -> order.getTotalAmount().compareTo(new BigDecimal("1000")) > 0)
                .map((key, order) -> KeyValue.pair(
                        "high-value-order-" + order.getOrderId(),
                        "High-value order detected: Order " + order.getOrderId() + 
                        " with amount $" + order.getTotalAmount()
                ))
                .to(ALERTS_TOPIC);

        // Order volume spikes detection
        parsedOrderStream
                .filter((key, order) -> "ORDER_CREATED".equals(order.getEventType()))
                .groupBy((key, order) -> "volume")
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
                .count()
                .toStream()
                .filter((key, count) -> count > 10) // Alert if more than 10 orders per minute
                .map((key, count) -> KeyValue.pair(
                        "volume-spike-" + key.window().startTime().toString(),
                        "Order volume spike detected: " + count + " orders in 1 minute"
                ))
                .to(ALERTS_TOPIC);
    }

    /**
     * Process user events for activity analytics
     */
    private void processUserEvents() {
        // Create KStream from user events topic
        KStream<String, String> userStream = streamsBuilder.stream(USER_EVENTS_TOPIC);

        // Parse user events and filter valid ones
        KStream<String, UserEvent> parsedUserStream = userStream
                .mapValues(this::parseUserEvent)
                .filter((key, value) -> value != null);

        // User activity tracking
        parsedUserStream
                .groupBy((key, user) -> user.getUserId().toString())
                .windowedBy(TimeWindows.of(Duration.ofMinutes(15)))
                .count()
                .toStream()
                .map((key, count) -> KeyValue.pair(
                        "user-activity-" + key.key(),
                        "User " + key.key() + " activity count: " + count
                ))
                .to(USER_ACTIVITY_TOPIC);

        // New user registration analytics
        parsedUserStream
                .filter((key, user) -> "USER_CREATED".equals(user.getEventType()))
                .groupBy((key, user) -> "new-users")
                .windowedBy(TimeWindows.of(Duration.ofMinutes(30)))
                .count()
                .toStream()
                .map((key, count) -> KeyValue.pair(
                        "new-users-" + key.window().startTime().toString(),
                        "New user registrations: " + count
                ))
                .to(ANALYTICS_TOPIC);

        // User activity spikes
        parsedUserStream
                .groupBy((key, user) -> "activity")
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .filter((key, count) -> count > 20) // Alert if more than 20 user activities per 5 minutes
                .map((key, count) -> KeyValue.pair(
                        "user-activity-spike-" + key.window().startTime().toString(),
                        "User activity spike detected: " + count + " activities in 5 minutes"
                ))
                .to(ALERTS_TOPIC);
    }

    /**
     * Process cross-stream analytics combining order and user data
     */
    private void processCrossStreamAnalytics() {
        // Create streams for cross-analysis
        KStream<String, String> orderStream = streamsBuilder.stream(ORDER_EVENTS_TOPIC);
        KStream<String, String> userStream = streamsBuilder.stream(USER_EVENTS_TOPIC);

        // Parse events
        KStream<String, OrderEvent> parsedOrderStream = orderStream
                .mapValues(this::parseOrderEvent)
                .filter((key, value) -> value != null);

        KStream<String, UserEvent> parsedUserStream = userStream
                .mapValues(this::parseUserEvent)
                .filter((key, value) -> value != null);

        // User revenue analysis (combine user and order data)
        KTable<String, UserEvent> userTable = parsedUserStream
                .groupBy((key, user) -> user.getUserId().toString())
                .reduce((user1, user2) -> user2); // Keep latest user data

        parsedOrderStream
                .filter((key, order) -> "ORDER_CREATED".equals(order.getEventType()))
                .groupBy((key, order) -> order.getUserId().toString())
                .windowedBy(TimeWindows.of(Duration.ofHours(1)))
                .aggregate(
                        () -> BigDecimal.ZERO,
                        (key, order, total) -> total.add(order.getTotalAmount()),
                        Materialized.with(Serdes.String(), new BigDecimalSerde())
                )
                .toStream()
                .map((key, revenue) -> KeyValue.pair(
                        "user-revenue-" + key.key(),
                        "User " + key.key() + " revenue in last hour: $" + revenue
                ))
                .to(REVENUE_ANALYTICS_TOPIC);
    }

    /**
     * Parse OrderEvent from JSON string
     * 
     * @param json the JSON string
     * @return OrderEvent object or null if parsing fails
     */
    private OrderEvent parseOrderEvent(String json) {
        try {
            return objectMapper.readValue(json, OrderEvent.class);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing OrderEvent: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parse UserEvent from JSON string
     * 
     * @param json the JSON string
     * @return UserEvent object or null if parsing fails
     */
    private UserEvent parseUserEvent(String json) {
        try {
            return objectMapper.readValue(json, UserEvent.class);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing UserEvent: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Custom Serde for BigDecimal values
     */
    private static class BigDecimalSerde extends Serdes.WrapperSerde<BigDecimal> {
        public BigDecimalSerde() {
            super(new BigDecimalSerializer(), new BigDecimalDeserializer());
        }
    }

    private static class BigDecimalSerializer implements org.apache.kafka.common.serialization.Serializer<BigDecimal> {
        @Override
        public byte[] serialize(String topic, BigDecimal data) {
            return data != null ? data.toString().getBytes() : null;
        }
    }

    private static class BigDecimalDeserializer implements org.apache.kafka.common.serialization.Deserializer<BigDecimal> {
        @Override
        public BigDecimal deserialize(String topic, byte[] data) {
            return data != null ? new BigDecimal(new String(data)) : null;
        }
    }
} 