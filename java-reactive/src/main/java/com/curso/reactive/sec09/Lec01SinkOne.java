package com.curso.reactive.sec09;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Sinks;

public class Lec01SinkOne {

	public static void main(String[] args) {
		
		demo2();

	}
	
	private static void demo1(){
		var sink = Sinks.one();
		var mono = sink.asMono();
		mono.subscribe(Util.createSubscriber());
		
//		sink.tryEmitValue("Hola");
//		sink.tryEmitValue("Adiós");
//		sink.tryEmitEmpty();
		sink.tryEmitError(new RuntimeException("oops"));
	}
	
	private static void demo2(){
        var sink = Sinks.one();
        var mono = sink.asMono();
        sink.tryEmitValue("hi");
        mono.subscribe(Util.createSubscriber("sam"));
        mono.subscribe(Util.createSubscriber("mike"));
    }


}
