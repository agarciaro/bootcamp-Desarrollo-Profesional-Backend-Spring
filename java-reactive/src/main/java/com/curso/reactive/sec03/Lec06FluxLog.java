package com.curso.reactive.sec03;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec06FluxLog {

	public static void main(String[] args) {

		Flux.range(1, 5)
			.log()
			.map(i -> Util.faker().hipster().word())
			.log()
			.subscribe(Util.createSubscriber());

	}

}
