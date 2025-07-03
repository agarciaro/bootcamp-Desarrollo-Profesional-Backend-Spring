package com.bootcamp.analytics.config;

import com.bootcamp.analytics.streams.AnalyticsStreamProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Streams Initializer
 * 
 * Initializes and builds the Kafka Streams topology on application startup.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Component
public class StreamsInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StreamsInitializer.class);

    @Autowired
    private AnalyticsStreamProcessor analyticsStreamProcessor;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing Kafka Streams topology...");
        
        try {
            analyticsStreamProcessor.buildTopology();
            logger.info("Kafka Streams topology built successfully");
        } catch (Exception e) {
            logger.error("Error building Kafka Streams topology: {}", e.getMessage(), e);
            throw e;
        }
    }
} 