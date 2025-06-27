package com.curso.reactive.sec03;

import java.time.LocalDate;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec01FluxJust {
	public static void main(String[] args) {
		Flux.just(1, 2, 3, 4, "Pepe", LocalDate.now())
			.subscribe(Util.createSubscriber());
	}
}
