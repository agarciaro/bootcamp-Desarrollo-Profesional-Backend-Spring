package com.curso.reactive.sec06;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec02HotPublisher {

	public static void main(String[] args) {

//		var movieFlux = movieStream().share();
		var movieFlux = movieStream().publish().refCount(1);

		Util.sleepSeconds(2);

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
		
		Util.sleepSeconds(15);

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
