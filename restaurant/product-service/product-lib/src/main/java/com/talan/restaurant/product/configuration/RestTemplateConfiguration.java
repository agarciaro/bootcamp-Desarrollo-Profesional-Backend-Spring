package com.talan.restaurant.product.configuration;

import java.time.Duration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
	
	 @Bean
	 @LoadBalanced
	 RestTemplate restTemplate() {
		 SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		 factory.setConnectTimeout(Duration.ofSeconds(5));
		 factory.setReadTimeout(Duration.ofSeconds(5));
		 
		 var restTemplate = new RestTemplate(factory);
		 return restTemplate;
	 }
	
}
