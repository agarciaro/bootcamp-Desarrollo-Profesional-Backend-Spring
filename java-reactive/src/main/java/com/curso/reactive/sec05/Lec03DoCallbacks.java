package com.curso.reactive.sec05;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec03DoCallbacks {
	
	public static void main(String[] args) {
		
		Flux.<Integer>create(fluxSink -> {
			log.info("producer begins");
			for (int i = 0; i < 4; i++) {
				fluxSink.next(i);
			}
			fluxSink.complete();
			// fluxSink.error(new RuntimeException("oops"));
			log.info("producer ends");
		})
			.doOnComplete(() -> log.info("doOnComplete-1"))
			.doFirst(() -> log.info("doFirst-1"))
			.doOnNext(item -> log.info("doOnNext-1: {}", item))
			.doOnSubscribe(subscription -> log.info("doOnSubscribe-1: {}", subscription))
			.doOnRequest(request -> log.info("doOnRequest-1: {}", request))
			.doOnError(error -> log.info("doOnError-1: {}", error.getMessage()))
			.doOnTerminate(() -> log.info("doOnTerminate-1")) // complete or error case
			.doOnCancel(() -> log.info("doOnCancel-1"))
			.doOnDiscard(Object.class, o -> log.info("doOnDiscard-1: {}", o))
			.doFinally(signal -> log.info("doFinally-1: {}", signal)) // finally irrespective of the reason
//			.take(2)
			.doOnComplete(() -> log.info("doOnComplete-2"))
			.doFirst(() -> log.info("doFirst-2"))
			.doOnNext(item -> log.info("doOnNext-2: {}", item))
			.doOnSubscribe(subscription -> log.info("doOnSubscribe-2: {}", subscription))
			.doOnRequest(request -> log.info("doOnRequest-2: {}", request))
			.doOnError(error -> log.info("doOnError-2: {}", error.getMessage()))
			.doOnTerminate(() -> log.info("doOnTerminate-2")) // complete or error case
			.doOnCancel(() -> log.info("doOnCancel-2"))
			.doOnDiscard(Object.class, o -> log.info("doOnDiscard-2: {}", o))
			.doFinally(signal -> log.info("doFinally-2: {}", signal)) // finally irrespective of the reason
			.take(3)
			.subscribe(Util.createSubscriber("sub"));

	}

}
