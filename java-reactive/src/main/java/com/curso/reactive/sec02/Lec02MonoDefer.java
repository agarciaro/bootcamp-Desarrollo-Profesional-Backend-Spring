package com.curso.reactive.sec02;

import java.util.List;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoDefer {
	public static void main(String[] args) {
//		 Mono.defer(Lec02MonoDefer::createPublisher)
//		    .subscribe(Util.createSubscriber("defer-sum"));
		 
		 
		 Mono<String> mono = Mono.defer(() -> Mono.just("Hola " + System.currentTimeMillis()));
		 mono.subscribe(System.out::println); // Imprime "Hola 16987654321"
		 Util.sleepSeconds(2); // Simula un retraso
		 mono.subscribe(System.out::println); // Imprime "Hola 16987664321" (nuevo timestamp)
	
	}
	
	private static Mono<Integer> createPublisher() {
		log.info("creating publisher");
        var list = List.of(1, 2, 3);
        Util.sleepSeconds(1);
        return Mono.fromSupplier(() -> sum(list));
	}
	
	// time-consuming business logic
    private static int sum(List<Integer> list) {
        log.info("finding the sum of {}", list);
        Util.sleepSeconds(3);
        return list.stream().mapToInt(a -> a).sum();
    }
}
