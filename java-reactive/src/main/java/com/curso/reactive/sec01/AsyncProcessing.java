 package com.curso.reactive.sec01;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncProcessing {
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
			final int messageId = i;
			CompletableFuture.runAsync(() -> processMessage(messageId), executor);
		}
		System.out.println("All messages submitted for processing.");
		Thread.sleep(10000); // Wait for all tasks to complete
	}

	private static void processMessage(int messageId) {
		try {
            Thread.sleep(1000);
            System.out.println("Processed message: " + messageId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
