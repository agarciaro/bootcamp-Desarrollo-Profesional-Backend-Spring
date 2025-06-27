package com.curso.reactive.sec04;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec04.helper.NameGenerator;

import reactor.core.publisher.Flux;

public class Lec02FluxCreateRefactor {

	public static void main(String[] args) {
		
		var generator = new NameGenerator();
        var flux = Flux.create(generator);
        flux.subscribe(Util.createSubscriber("sub1"));
//        flux.subscribe(Util.createSubscriber("sub2"));

        for (int i = 0; i < 2; i++) {
            generator.generate();
        }

	}

}
