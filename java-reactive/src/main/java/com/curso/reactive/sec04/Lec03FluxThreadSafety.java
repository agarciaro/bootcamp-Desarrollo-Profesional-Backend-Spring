package com.curso.reactive.sec04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec04.helper.NameGenerator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class Lec03FluxThreadSafety {

	public static void main(String[] args) {
		demo3();
	}
	
	private static void demo1(){
        var list = new ArrayList<Integer>();
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
        Util.sleepSeconds(3);
        log.info("list size: {}", list.size());
    }
	
	private static void demo2(){
        var list = new ArrayList<String>();
        var generator = new NameGenerator();
        var flux = Flux.create(generator);
        flux.subscribe(list::add);

        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                generator.generate();
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
        Util.sleepSeconds(3);
        log.info("list size: {}", list.size());
    }
	
	private static void demo3(){
        // Crear un Arraylist y hacerlo Thread-safe
		var list = Collections.synchronizedList(new ArrayList<String>());
//        var generator = new NameGenerator();
        
        // FluxSink thread-safe
//        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
		Sinks.Many<String> sink = Sinks.many().replay().limit(10000);
        Flux<String> flux = sink.asFlux();
        
        AtomicInteger completedThreads = new AtomicInteger(0);
        int totalThreads = 10;
        
        flux.doOnNext(item -> log.info("Elemento emitido: {}", item))
        	.subscribe(
        		list::add, //onNext
        		error -> log.error("Error {}", error.getMessage()), //onError
        		() -> {
        			log.info("Subs: list size: {}", list.size()); 
        			if(completedThreads.get() == totalThreads) {
        				log.info("Todas las emisiones procesadas. Final size:{}", list.size());
        			}
        		} //onComplete
        	);
        
        
//        flux.subscribe(list::add);

        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                sink.tryEmitNext("Name-" + i);
//                log.info("{}", i);
            }
            completedThreads.incrementAndGet();
            if(completedThreads.get() == totalThreads) {
            	sink.tryEmitComplete();
            }
        };
        
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
        
//        Util.sleepSeconds(10);
//        sink.tryEmitComplete();
//        
//        flux.blockLast();
//        
//        log.info("list size: {}", list.size());
    }

}
