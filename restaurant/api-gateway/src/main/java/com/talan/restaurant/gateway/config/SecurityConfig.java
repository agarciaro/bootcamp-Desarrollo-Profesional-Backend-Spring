package com.talan.restaurant.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf().disable() // Disable CSRF for simplicity, not recommended for production
				.authorizeExchange().pathMatchers("/actuator/**").permitAll() // Public endpoints
				.anyExchange().authenticated() // All other endpoints require authentication
				.and().oauth2Login(Customizer.withDefaults()); // Enable OAuth2 login

		return http.build();
	}
	
}
