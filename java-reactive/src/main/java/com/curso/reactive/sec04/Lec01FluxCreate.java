package com.curso.reactive.sec04;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec01FluxCreate {

	public static void main(String[] args) {
		
		Flux.create(fluxSink -> {
			for (int i = 0; i < 10; i++) {
				fluxSink.next(Util.faker().country().name());		
			}
			fluxSink.complete();
		}).subscribe(Util.createSubscriber());

	}

}
