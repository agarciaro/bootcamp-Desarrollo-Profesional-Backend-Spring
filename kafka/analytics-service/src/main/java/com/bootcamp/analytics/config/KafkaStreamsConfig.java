package com.bootcamp.analytics.config;

import com.bootcamp.analytics.model.OrderEvent;
import com.bootcamp.analytics.model.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Streams Configuration
 * 
 * Configures Kafka Streams for real-time analytics processing.
 * Sets up serialization, deserialization, and stream processing properties.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    /**
     * Configure Kafka Streams properties
     * 
     * @return StreamsConfig with all necessary properties
     */
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once_v2");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        
        // Consumer and Producer configurations
        props.put(StreamsConfig.consumerPrefix("auto.offset.reset"), "earliest");
        props.put(StreamsConfig.consumerPrefix("enable.auto.commit"), "false");
        props.put(StreamsConfig.producerPrefix("acks"), "all");
        props.put(StreamsConfig.producerPrefix("retries"), 3);
        
        return new StreamsConfig(props);
    }

    /**
     * Configure ObjectMapper for JSON serialization
     * 
     * @return ObjectMapper with JavaTimeModule
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Create Serde for OrderEvent
     * 
     * @param objectMapper the ObjectMapper bean
     * @return JsonSerde for OrderEvent
     */
    @Bean
    public Serde<OrderEvent> orderEventSerde(ObjectMapper objectMapper) {
        JsonSerde<OrderEvent> serde = new JsonSerde<>(OrderEvent.class, objectMapper);
        serde.ignoreTypeHeaders();
        return serde;
    }

    /**
     * Create Serde for UserEvent
     * 
     * @param objectMapper the ObjectMapper bean
     * @return JsonSerde for UserEvent
     */
    @Bean
    public Serde<UserEvent> userEventSerde(ObjectMapper objectMapper) {
        JsonSerde<UserEvent> serde = new JsonSerde<>(UserEvent.class, objectMapper);
        serde.ignoreTypeHeaders();
        return serde;
    }

    /**
     * Create Serde for String values
     * 
     * @return String Serde
     */
    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }
} 