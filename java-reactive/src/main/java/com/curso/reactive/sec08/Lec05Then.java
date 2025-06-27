package com.curso.reactive.sec08;

import java.time.Duration;
import java.util.List;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec05Then {
	
	public static void main(String[] args) {

        var records = List.of("a", "b", "c");

        saveRecords(records)
                .then(sendNotification(records)) // only in case of success
                .subscribe(Util.createSubscriber());


        Util.sleepSeconds(5);

    }

    private static Flux<String> saveRecords(List<String> records) {
        return Flux.fromIterable(records)
                   .map(r -> "saved " + r)
                   .delayElements(Duration.ofMillis(500));
    }

    private static Mono<Void> sendNotification(List<String> records) {
        return Mono.fromRunnable(() -> log.info("all these {} records saved successfully", records));
    }
	
}
