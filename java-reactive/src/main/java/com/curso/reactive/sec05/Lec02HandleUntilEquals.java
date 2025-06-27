package com.curso.reactive.sec05;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec02HandleUntilEquals {

	public static void main(String[] args) {
		Flux.<String>generate(sink -> sink.next(Util.faker().country().name()))
			.handle((item, sink) -> {
				sink.next(item);
				if(item.equalsIgnoreCase("Canada")) {
					sink.complete();
				}
			})
			.subscribe(Util.createSubscriber());

	}

}
