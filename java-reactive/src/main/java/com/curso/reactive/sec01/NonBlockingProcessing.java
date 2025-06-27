package com.curso.reactive.sec01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SubmissionPublisher;

public class NonBlockingProcessing {
	
	public static final int MESSAGE_COUNT = 5;
	
	public static void main(String[] args) throws InterruptedException {
		SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
		CountDownLatch latch = new CountDownLatch(MESSAGE_COUNT);
		publisher.subscribe(new MessageSubscriber(latch));
		
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			publisher.submit(i);
		}
		System.out.println("All messages submitted for processing.");
		publisher.close();
		latch.await(); // Wait for all messages to be processed
		System.out.println("Process complete.");
	}
	
}
