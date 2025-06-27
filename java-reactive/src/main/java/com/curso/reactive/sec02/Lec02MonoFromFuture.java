package com.curso.reactive.sec02;

import java.util.concurrent.CompletableFuture;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoFromFuture {
	public static void main(String[] args) {
		
		//CompletableFuture< String> future = getName();
		//log.info("Future 1: {}", future);
		
		//future.thenAccept(name -> log.info("Received name: {}", name));
		//log.info("Future 2: {}", future);
		
		Mono<String> monoFromFuture = Mono.fromSupplier(() -> getString());
		log.info("I am here...");
		monoFromFuture.subscribe(Util.createSubscriber());
		
		log.info("almost finished...");
		
		Util.sleepSeconds(2); 
	}
	
	private static String getString() {
		Util.sleepSeconds(2); 
		return "Pepe";
	}
	
	private static CompletableFuture<String> getName() {
		Util.sleepSeconds(2); 
		return CompletableFuture.completedFuture("Pepe");
		/*return CompletableFuture.supplyAsync(
			() -> { 
				Util.sleepSeconds(1); // Simular una operación que toma tiempo
				log.info("Calculando nombre del usuario");
				return Util.faker().name().firstName();
			}
		);*/
	}

}
