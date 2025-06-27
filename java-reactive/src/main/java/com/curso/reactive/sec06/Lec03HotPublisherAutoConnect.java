package com.curso.reactive.sec06;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec03HotPublisherAutoConnect {

	public static void main(String[] args) {

		var movieFlux = movieStream().publish().autoConnect(0);

		Util.sleepSeconds(7);

		movieFlux
			.take(2)
			.subscribe(Util.createSubscriber("Pepe"));

		Util.sleepSeconds(3);

		movieFlux
			.take(3)
			.subscribe(Util.createSubscriber("Juan"));
		
		Util.sleepSeconds(4);

		movieFlux
			.subscribe(Util.createSubscriber("María"));
		
		Util.sleepSeconds(12);
		
		movieFlux
		.subscribe(Util.createSubscriber("Ultimo sub"));
		
		Util.sleepSeconds(2);
		
	}

	private static Flux<String> movieStream() {
		return Flux.generate(() -> {
			log.info("Recibido request");
			return 1;
		}, (state, sink) -> {
			var scene = "Escena de peli: " + state;
			log.info("Reproduciendo {}", scene);
			sink.next(scene);
			return ++state;
		}).take(10).delayElements(Duration.ofSeconds(1)).cast(String.class);

	}

}
