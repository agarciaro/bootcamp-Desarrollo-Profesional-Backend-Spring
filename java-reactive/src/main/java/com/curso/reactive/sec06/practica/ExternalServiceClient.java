package com.curso.reactive.sec06.practica;

import java.util.Objects;

import com.curso.reactive.common.AbstractHttpClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class ExternalServiceClient extends AbstractHttpClient {
	
	private Flux<Order> orderFlux;
	
	public Flux<Order> orderStream() {
		if(Objects.isNull(orderFlux)) {
			this.orderFlux = getOrderStream();
		}
		return this.orderFlux;
	}
	
	private Flux<Order> getOrderStream() {
		return this.client.get()
				.uri("/demo04/orders/stream")
				.responseContent()
				.asString()
				.map(this::parse)
				.doOnNext( o -> log.info("{}", o))
				.publish()
				.refCount(2);
	}
	
	private Order parse(String message) {
		message = message.replaceAll("\\s+", "");
		var values = message.split(":");
		log.info("<<< {}", message);
		return new Order(values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]));
	}
	
}
