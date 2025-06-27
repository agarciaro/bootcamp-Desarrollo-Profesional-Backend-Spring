package com.curso.reactive.sec02;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoFromRunnable {
	public static void main(String[] args) {
		
		getById(3).subscribe(Util.createSubscriber("user-1"));
		
		String name = Mono.just("Pepe").block();
		log.info("User name: {}", name);
		
		//var response = client.get("http://localhost:8080/users/1").block();
		//log.info("Response: {}", response);
		
		Mono.just("Pepe").subscribe(nombre -> log.info("User name: {}", nombre));
	
	}
	
	private static Mono<String> getById(int userId) {
		if(userId == 1) {
			return Mono.fromSupplier(() -> Util.faker().name().fullName());
		}
		return Mono.fromRunnable(() -> notifyBusiness(userId));
	}
	
	private static void notifyBusiness(int userId){
		Util.sleepSeconds(3);
        log.info("notifying business on unavailable User {}", userId);
    }

}
