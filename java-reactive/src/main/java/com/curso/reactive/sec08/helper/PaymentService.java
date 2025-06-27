package com.curso.reactive.sec08.helper;

import java.util.Map;

import reactor.core.publisher.Mono;

public class PaymentService {
	
	private static final Map<Integer, Integer> userBalanceTable = Map.of(
            1, 100,
            2, 200,
            3, 300
    );

    public static Mono<Integer> getUserBalance(Integer userId) {
        return Mono.fromSupplier(() -> userBalanceTable.get(userId));
    }
	
}
