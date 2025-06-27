package com.curso.reactive.sec06.practica;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.curso.reactive.sec06.practica.Order;

import reactor.core.publisher.Flux;

public class RevenueService implements OrderProcessor {
	
	private final Map<String, Integer> db = new HashMap<>();
	
	@Override
	public void consume(Order order) {
		var currentRevenue = db.getOrDefault(order.name(), 0);
		var updatedRevenue = currentRevenue + order.price();
		db.put(order.name(), updatedRevenue);
		
	}

	@Override
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(2))
				.map(i -> this.db.toString());
	}

}
