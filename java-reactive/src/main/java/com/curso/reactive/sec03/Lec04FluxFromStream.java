package com.curso.reactive.sec03;

import java.util.List;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec04FluxFromStream {
	
	public static void main(String[] args) {

        var list = List.of(1,2,3,4);
        var stream = list.stream();

//        stream.forEach(System.out::println);
//        stream.forEach(System.out::println);

        var flux = Flux.fromStream(list::stream);

        flux.subscribe(Util.createSubscriber("sub1"));
        flux.subscribe(Util.createSubscriber("sub2"));




    }
	
}
