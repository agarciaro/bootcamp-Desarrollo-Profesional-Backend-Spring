package com.curso.reactive.sec02;

import java.util.List;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoFromSupplier {
	public static void main(String[] args) {
		
		var list = List.of(1, 2, 3, 4, 5);
		Mono.fromSupplier(() -> sum(list))
		    .subscribe(Util.createSubscriber("sum"));
	
	}

	private static int sum(List<Integer> list) {
		log.info("Calculating sum of {}", list);
        return list.stream().mapToInt(Integer::intValue).sum();
	}

}
