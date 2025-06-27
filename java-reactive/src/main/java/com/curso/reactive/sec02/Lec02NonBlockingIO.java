package com.curso.reactive.sec02;

import com.curso.reactive.common.ExternalServiceClient;
import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec02NonBlockingIO {

	public static void main(String[] args) {
		var client = new ExternalServiceClient();
		
		log.info("Starting non-blocking IO with Reactor...");
		for (int i = 1; i < 1000; i++) {
			client.getProductName(i)
				.subscribe(Util.createSubscriber("Pepe"));
		}
		
		Util.sleepSeconds(2); // Esperar para que se completen las peticiones

	}

}
