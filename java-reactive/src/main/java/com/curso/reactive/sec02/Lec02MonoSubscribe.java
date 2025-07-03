package com.curso.reactive.sec02;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoSubscribe {
	public static void main(String[] args) {
		var publisher = Mono.just(10000);
		
		publisher.subscribe(
			i -> log.info("Received value: {}", i),
			err -> log.error("Error received: {}", err.getMessage()),
			() -> log.info("Completed with no errors")
			//,
			//subscription -> subscription.request(1) //No tiene sentido, pero se puede hacer
		);
		
		publisher.subscribe(
				i -> log.info("2Received value: {}", i),
				err -> log.error("2Error received: {}", err.getMessage()),
				() -> log.info("2Completed with no errors"));
	
	}
}
