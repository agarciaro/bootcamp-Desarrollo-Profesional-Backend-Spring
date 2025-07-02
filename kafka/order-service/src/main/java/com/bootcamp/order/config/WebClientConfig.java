package com.bootcamp.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient Configuration
 * 
 * Provides WebClient beans for reactive HTTP communication.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@Configuration
public class WebClientConfig {

    /**
     * Create a load-balanced WebClient.Builder
     * 
     * @return WebClient.Builder with load balancing support
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Create a standard WebClient.Builder
     * 
     * @return WebClient.Builder without load balancing
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
} 