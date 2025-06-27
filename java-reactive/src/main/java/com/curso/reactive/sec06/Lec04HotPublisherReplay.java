package com.curso.reactive.sec06;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec04HotPublisherReplay {

	public static void main(String[] args) {

		var stockFlux = stockStream().replay().autoConnect(0);

		Util.sleepSeconds(5);
		
		log.info("Pepe se une");
		stockFlux
			.take(7)
			.subscribe(Util.createSubscriber("Pepe"));

		Util.sleepSeconds(3);
		
		log.info("Juan se une");
		stockFlux
			.replay(2)
			.take(3)
			.subscribe(Util.createSubscriber("Juan"));
		
		Util.sleepSeconds(10);
		
	}

	private static Flux<Integer> stockStream() {
		return Flux.generate(sink -> sink.next(Util.faker().random().nextInt(10, 100)))
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(price -> log.info(">>> price: {}", price))
				.cast(Integer.class);

	}

}
