package com.bootcamp.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Analytics Service Application
 * 
 * This microservice provides real-time analytics using Kafka Streams:
 * - Processes order and user events in real-time
 * - Calculates real-time metrics and KPIs
 * - Provides analytics dashboards and APIs
 * - Generates insights from event streams
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }
} 