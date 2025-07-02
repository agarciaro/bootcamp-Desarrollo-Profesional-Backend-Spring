package com.bootcamp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Service Application
 * 
 * This service provides centralized configuration management for all microservices.
 * It stores configuration files in a Git repository and serves them to other services.
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
} 