package com.curso.reactive.sec02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.curso.reactive.common.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class Lec02MonoFromCallable {
	public static void main(String[] args) {
		
		var list = List.of(1, 2, 3, 4, 5); 
		final List<Integer> emptyList = new ArrayList<>();
		Mono.fromCallable(() -> sum(emptyList))
		    .subscribe(Util.createSubscriber("sum"));
	
	}

	private static int sum(List<Integer> list) throws Exception {
		log.info("Calculating sum of {}", list);
		if (list.isEmpty()) {
			throw new Exception("List cannot be empty");
		}
        return list.stream().mapToInt(Integer::intValue).sum();
	}

}
