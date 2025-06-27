package com.curso.reactive.sec02;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoEmptyError {
	public static void main(String[] args) {
		
		// String username = getById(1);
		getById(209).subscribe(Util.createSubscriber());
	
	}

	private static Mono<String> getById(int userId) {
		return switch (userId) {
			case 1 -> Mono.just("Pepe");
			case 2 -> Mono.empty();
			default -> Mono.error(new RuntimeException("Usuario no encontrado"));
		};
	}
	
//	findById(Mono.just(3));
//	findById(Mono<Integer>);
}
