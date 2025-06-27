package com.curso.reactive.sec07;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Lec03BackPressureStrategies {

	public static void main(String[] args) {

//		System.setProperty("reactor.bufferSize.small", "16");

		var producer = Flux.create(sink -> {
			for (int i = 1; i <= 100 && !sink.isCancelled(); i++) {
				log.info("generating {}", i);
				sink.next(i);
				Util.sleepMillis(50);
			}
			sink.complete();
		}).cast(Integer.class).subscribeOn(Schedulers.parallel());

		producer
			.onBackpressureBuffer()
//			.onBackpressureBuffer(10)
//			.onBackpressureError()
//			.onBackpressureDrop()
//			.onBackpressureLatest()
			.log()
//			.limitRate(1)
			.publishOn(Schedulers.boundedElastic())
			.map(Lec03BackPressureStrategies::timeConsumingTask)
			.subscribe();

		Util.sleepSeconds(60);

	}

	private static int timeConsumingTask(int i) {
		log.info("received: {}", i);
		Util.sleepSeconds(1);
		return i;
	}

}
