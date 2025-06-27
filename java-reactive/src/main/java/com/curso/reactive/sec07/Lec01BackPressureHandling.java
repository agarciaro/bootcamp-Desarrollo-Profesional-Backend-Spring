package com.curso.reactive.sec07;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Lec01BackPressureHandling {
	
	public static void main(String[] args) {
		
//		System.setProperty("reactor.bufferSize.small", "16");
		
		var producer = Flux.generate(
				  () -> 1,
				  (state, sink) -> {
					  log.info("generating {}", state);
					  sink.next(state);
					  return ++state;
				  }
				).cast(Integer.class)
				.subscribeOn(Schedulers.parallel())
				;
		
		producer
			.limitRate(5)
			.publishOn(Schedulers.boundedElastic())
			.map(Lec01BackPressureHandling::timeConsumingTask)
			.subscribe(Util.createSubscriber("sub1"));
		
		producer
        .take(100)
        .publishOn(Schedulers.boundedElastic())
        .subscribe(Util.createSubscriber("sub2"));
		
		Util.sleepSeconds(60);

	}
	
	private static int timeConsumingTask(int i) {
        Util.sleepSeconds(1);
        return i;
    }

}
