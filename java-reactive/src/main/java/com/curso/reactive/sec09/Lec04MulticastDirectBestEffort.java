package com.curso.reactive.sec09;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec04MulticastDirectBestEffort {

	public static void main(String[] args) {
		demo3();
		Util.sleepSeconds(10);
	}

	/*
	 * When we have multiple subscribers, if one subscriber is slow, we might not be
	 * able to safely deliver messages to all the subscribers / other fast
	 * subscribers might not get the messages.
	 */
	private static void demo1() {

		System.setProperty("reactor.bufferSize.small", "16");

		// handle through which we would push items
		// onBackPressureBuffer - bounded queue
		var sink = Sinks.many().multicast().onBackpressureBuffer();

		// handle through which subscribers will receive items
		var flux = sink.asFlux();

		flux.subscribe(Util.createSubscriber("sam"));
		flux.delayElements(Duration.ofMillis(200)).subscribe(Util.createSubscriber("mike"));

		for (int i = 1; i <= 100; i++) {
			var result = sink.tryEmitNext(i);
			log.info("item: {}, result: {}", i, result);
		}

	}

	/*
	 * directBestEffort - focus on the fast subscriber and ignore the slow
	 * subscriber
	 */
	private static void demo2() {

		System.setProperty("reactor.bufferSize.small", "16");

		// handle through which we would push items
		// onBackPressureBuffer - bounded queue
		var sink = Sinks.many().multicast().directBestEffort();

		// handle through which subscribers will receive items
		var flux = sink.asFlux();

		flux.subscribe(Util.createSubscriber("sam"));
		flux.onBackpressureBuffer()
				.delayElements(Duration.ofMillis(200)).subscribe(Util.createSubscriber("mike"));

		for (int i = 1; i <= 100; i++) {
			var result = sink.tryEmitNext(i);
			log.info("item: {}, result: {}", i, result);
		}

	}
	
	private static void demo3(){

        // handle through which we would push items
//        var sink = Sinks.many().replay().all();
		var sink = Sinks.many().replay().limit(1);

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.createSubscriber("sam"));
        flux.subscribe(Util.createSubscriber("mike"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);

        flux.subscribe(Util.createSubscriber("jake"));
        sink.tryEmitNext("new message");

    }

}
