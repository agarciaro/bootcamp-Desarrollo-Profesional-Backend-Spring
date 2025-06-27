package com.curso.reactive.sec06.practica;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.curso.reactive.sec06.practica.Order;

import reactor.core.publisher.Flux;

public class InventoryService implements OrderProcessor {
	
	private final Map<String, Integer> db = new HashMap<>();
	
	@Override
	public void consume(Order order) {
		var currentInventory = db.getOrDefault(order.name(), 500);
		var updatedQuantity = currentInventory - order.quantity();
		db.put(order.name(), updatedQuantity);
	}

	@Override
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(2))
				.map(i -> this.db.toString());
	}

}
