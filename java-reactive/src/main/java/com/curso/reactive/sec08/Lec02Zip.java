package com.curso.reactive.sec08;

import java.time.Duration;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec02Zip {

	record Car(String body, String engine, String tires){}

    public static void main(String[] args) {

        Flux.zip(getBody(), getEngine(), getTires())
                .map(t -> new Car(t.getT1(), t.getT2(), t.getT3()))
                .subscribe(Util.createSubscriber());
        
        log.info("Aquí estoy");

        Util.sleepSeconds(5);
    }

    private static Flux<String> getBody(){
        return Flux.range(1, 5)
                   .map(i -> "body-" + i)
                   .delayElements(Duration.ofMillis(100));
    }

    private static Flux<String> getEngine(){
        return Flux.range(1, 3)
                   .map(i -> "engine-" + i)
                   .delayElements(Duration.ofMillis(200));
    }

    private static Flux<String> getTires(){
        return Flux.range(1, 10)
                   .map(i -> "tires-" + i)
                   .delayElements(Duration.ofMillis(75));
    }

}
