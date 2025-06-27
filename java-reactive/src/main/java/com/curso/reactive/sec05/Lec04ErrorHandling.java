package com.curso.reactive.sec05;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class Lec04ErrorHandling {

	public static void main(String[] args) {
//		onErrorResume();
//		onErrorMap();
//		onErrorContinue();
//		retry();
//		retryWhen();
		usingWhen();

	}

	private static void onErrorResume() {
		Flux.just(1, 2, 0, 4).map(i -> 10 / i).onErrorResume(e -> Flux.just(-1, -2)).subscribe(System.out::println);
	}

	private static void onErrorMap() {
		Flux.just(1, 2, 0, 4).map(i -> 10 / i)
				.onErrorMap(e -> new RuntimeException("División fallida: " + e.getMessage()))
				.subscribe(v -> log.info("{}", v), e -> log.error("Error: {}", e.getMessage()));
	}

	private static void onErrorContinue() {
		Flux.just(1, 2, 0, 4).map(i -> {
			if (i == 0)
				throw new RuntimeException("División por cero");
			return 10 / i;
		}).onErrorContinue((err, item) -> log.warn("Ignorado: {}, ex: {} ", item, err.getMessage()))
				.subscribe(System.out::println);
	}

	private static void retry() {
		Flux.just(1, 2, 0, 4).map(i -> {
			if (i == 0)
				throw new RuntimeException("Temporal");
			return 10 / i;
		})
		.retry(2)
		.subscribe(
				v -> log.info("{}", v), 
				e -> log.error("Error final: {}", e.getMessage())
		);
	}
	
	private static void retryWhen() {
		Flux.just(1, 2, 0)
	    .map(i -> {
	        if (i == 0) throw new RuntimeException("Temporal");
	        return 10 / i;
	    })
	    .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
	    .subscribe(
				v -> log.info("{}", v), 
				e -> log.error("Error final: {}", e.getMessage())
		);
		
		Util.sleepSeconds(4);
	}
	
	private static void usingWhen() {
		Mono.usingWhen(
			Mono.just("recurso"),
			res -> Mono.error(new RuntimeException("Fallo")),
			res -> Mono.just("Limpieza")
		)
		.onErrorResume(e -> Mono.just("Recuperado"))
		.subscribe(item -> log.info("item: {}", item));
		
	}

}
