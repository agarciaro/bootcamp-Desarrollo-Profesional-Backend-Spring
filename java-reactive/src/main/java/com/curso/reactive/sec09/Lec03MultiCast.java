package com.curso.reactive.sec09;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Sinks;

public class Lec03MultiCast {

	public static void main(String[] args) {
		demo2();

	}
	
	private static void demo1(){

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

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
	
	// warmup
    private static void demo2(){

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);

        flux.subscribe(Util.createSubscriber("sam"));
        flux.subscribe(Util.createSubscriber("mike"));
        flux.subscribe(Util.createSubscriber("jake"));

        sink.tryEmitNext("new message");

    }

}
