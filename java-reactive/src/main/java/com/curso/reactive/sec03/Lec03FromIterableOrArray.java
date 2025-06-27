package com.curso.reactive.sec03;

import java.util.List;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec03FromIterableOrArray {
	public static void main(String[] args) {
		
		var list = List.of("a", "b", "c");
		
		Flux.fromIterable(list)
			.subscribe(Util.createSubscriber());
		
		Integer[] array = {1,2,3,4,5,6};
		Flux.fromArray(array)
			.subscribe(Util.createSubscriber());
	}
}
