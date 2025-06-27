package com.curso.reactive.sec05;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec01Handle {
	
	public static void main(String[] args) {
		
		Flux.range(1, 10)
			.handle((item, sink) -> {
				switch (item) {
				case 1 -> sink.next(-2);
				case 4 -> {}
				case 7 -> sink.error(new RuntimeException("Ups"));
				default -> sink.next(item);
				}
			}).subscribe(Util.createSubscriber());
	}
	
}
