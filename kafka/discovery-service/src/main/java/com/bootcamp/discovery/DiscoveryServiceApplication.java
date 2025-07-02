package com.bootcamp.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Discovery Service Application
 * 
 * This service acts as a service registry using Netflix Eureka.
 * All microservices will register themselves with this discovery service
 * so they can find and communicate with each other.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
} 