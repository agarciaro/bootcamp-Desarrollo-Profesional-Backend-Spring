package com.curso.reactive.sec03;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec08NonBlockingStreaming {

	public static void main(String[] args) {

		Flux.interval(Duration.ofMillis(1000))
			.map(i -> Util.faker().name().firstName()).subscribe(Util.createSubscriber());

		Util.sleepSeconds(5);

	}

}
