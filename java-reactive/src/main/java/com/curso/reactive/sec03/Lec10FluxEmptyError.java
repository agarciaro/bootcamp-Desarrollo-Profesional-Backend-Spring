package com.curso.reactive.sec03;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec10FluxEmptyError {

	public static void main(String[] args) {

		Flux.empty()
			.subscribe(Util.createSubscriber());

		Flux.error(new RuntimeException("oops"))
			.subscribe(Util.createSubscriber());

	}

}
