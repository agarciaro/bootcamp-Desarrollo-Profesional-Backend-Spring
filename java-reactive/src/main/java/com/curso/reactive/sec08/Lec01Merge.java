package com.curso.reactive.sec08;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec01Merge {

	public static void main(String[] args) {
		
		demo1();
		Util.sleepSeconds(3);
	}
	
	
	private static void demo1() {
		Flux.merge(producer1(), producer2(), producer3())
			.take(2)
			.subscribe(Util.createSubscriber());
	}
	
	
	private static Flux<Integer> producer1() {
        return Flux.just(1, 2, 3)
                   .transform(Util.fluxLogger("producer1"))
                   .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2() {
        return Flux.just(51, 52, 53)
                   .transform(Util.fluxLogger("producer2"))
                   .delayElements(Duration.ofMillis(11));
    }

    private static Flux<Integer> producer3() {
        return Flux.just(11, 12, 13)
                   .transform(Util.fluxLogger("producer3"))
                   .delayElements(Duration.ofMillis(12));
    }

}
