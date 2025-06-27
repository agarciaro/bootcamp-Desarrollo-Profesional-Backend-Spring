package com.curso.reactive.sec08;

import com.curso.reactive.common.Util;

import reactor.core.publisher.Flux;

public class Lec04CollectList {
	
	public static void main(String[] args) {
		Flux.range(1, 10)
		  .collectList()
		  .subscribe(Util.createSubscriber());
	}
	
}
