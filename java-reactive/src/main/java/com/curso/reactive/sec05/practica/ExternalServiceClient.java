package com.curso.reactive.sec05.practica;

import java.time.Duration;

import com.curso.reactive.common.AbstractHttpClient;

import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {
	
	public Mono<String> getProductName(int productId) {
		var defaultPath = "/demo03/product/" + productId;
		var timeoutPath = "/demo03/timeout-fallback/product/" + productId;
		var emptyPath = "/demo03/empty-fallback/product/" + productId;
		return getProductName(defaultPath)
				.timeout(Duration.ofSeconds(1), getProductName(timeoutPath))
				.switchIfEmpty(getProductName(emptyPath));
	}
	
	private Mono<String> getProductName(String path) {
		return this.client.get()
				.uri(path)
				.responseContent()
				.asString()
				.next();
	}
	
}
