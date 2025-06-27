package com.curso.reactive.common;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {
	
	public Mono<String> getProductName(int productId) {
        return this.client
        		.get()
        		.uri("/demo01/product/" + productId)
        		.responseContent()
        		.asString()
        		.next();
	}
	
	public Flux<Integer> getPriceChanges() {
        return this.client.get()
                .uri("/demo02/stock/stream")
                .responseContent()
                .asString()
                .map(Integer::parseInt);
    }
	
}
