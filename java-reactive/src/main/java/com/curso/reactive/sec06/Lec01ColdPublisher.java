package com.curso.reactive.sec06;

import java.util.concurrent.atomic.AtomicInteger;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec01ColdPublisher {

	public static void main(String[] args) {
		
		AtomicInteger atomicInteger = new AtomicInteger(0);
		
		var flux = Flux.create(fluxSink -> {
            log.info("invoked");
            for (int i = 0; i < 3; i++) {
                fluxSink.next(atomicInteger.incrementAndGet());
            }
            fluxSink.complete();
        });

        flux.subscribe(Util.createSubscriber("sub1"));
        flux.subscribe(Util.createSubscriber("sub2"));

	}

}
