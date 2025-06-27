package com.curso.reactive.sec06.practica;

import com.curso.reactive.sec06.practica.Order;

import reactor.core.publisher.Flux;

public interface OrderProcessor {
	
	void consume(Order order);
	
	Flux<String> stream();
	
}
