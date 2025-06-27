package com.curso.reactive.sec01;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class AsyncVsReactorExample {

	private static final Random RANDOM = new Random();
	private static final int NUM_REQUESTS = 100;
	private static final int MAX_CONCURRENCY = 10;

	private static String simulateHttpRequest(int requestId) {
		try {
			int delay = 100 + RANDOM.nextInt(400); // 100-500 ms
			Thread.sleep(delay);
			return "Response for request " + requestId + " (delay: " + delay + "ms)";
		} catch (InterruptedException e) {
			return "Error in request " + requestId;
		}
	}

	public static void runCompletableFutureExample() {
		System.out.println("=== CompletableFuture Example ===");
		long startTime = System.currentTimeMillis();

		ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENCY);

		List<CompletableFuture<String>> futures = IntStream.range(0, NUM_REQUESTS)
				.mapToObj(i -> CompletableFuture.supplyAsync(() -> simulateHttpRequest(i), executor))
				.collect(Collectors.toList());

		CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		allDone.thenRun(() -> {
			List<String> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
			System.out.println("Processed " + results.size() + " requests");
			System.out.println("First 5 results: " + results.subList(0, Math.min(5, results.size())));
			System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");
		}).join();

		executor.shutdown();
	}

	// Implementación con Project Reactor
	public static void runReactorExample() {
		System.out.println("\n=== Project Reactor Example ===");
		long startTime = System.currentTimeMillis();

		Flux.range(0, NUM_REQUESTS).parallel(MAX_CONCURRENCY).runOn(Schedulers.parallel())
				.map(i -> simulateHttpRequest(i)).sequential().collectList().doOnSuccess(results -> {
					System.out.println("Processed " + results.size() + " requests");
					System.out.println("First 5 results: " + results.subList(0, Math.min(5, results.size())));
					System.out.println("Total time: " + (System.currentTimeMillis() - startTime) + "ms");
				}).block();
	}

	public static void main(String[] args) {
		runCompletableFutureExample();
		runReactorExample();
	}
}