package com.curso.reactive.sec03;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec05FluxRange {

	public static void main(String[] args) {

		Flux.range(3, 10).subscribe(Util.createSubscriber());

		// generate 10 random first names
		Flux.range(1, 10).map(i -> Util.faker().name().firstName()).subscribe(Util.createSubscriber());

	}

}
