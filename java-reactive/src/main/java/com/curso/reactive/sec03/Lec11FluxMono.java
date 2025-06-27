package com.curso.reactive.sec03;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec11FluxMono {

	public static void main(String[] args) {

		monoToFlux();
		fluxToMono();

	}

	private static void fluxToMono() {
		var flux = Flux.range(1, 10);
		Mono.from(flux).subscribe(Util.createSubscriber());
	}

	private static void monoToFlux() {
		var mono = getUsername(1);
		save(Flux.from(mono));
	}

	private static Mono<String> getUsername(int userId) {
		return switch (userId) {
		case 1 -> Mono.just("sam");
		case 2 -> Mono.empty(); // null
		default -> Mono.error(new RuntimeException("invalid input"));
		};
	}

	private static void save(Flux<String> flux) {
		flux.subscribe(Util.createSubscriber());
	}

}
