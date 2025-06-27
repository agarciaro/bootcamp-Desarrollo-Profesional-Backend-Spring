package com.curso.reactive.sec01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

public class MessageSubscriber implements Flow.Subscriber<Integer> {
	
	private Flow.Subscription subscription;
	private final CountDownLatch latch;
	
	public MessageSubscriber(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(Integer item) {
		System.out.println("Processed message: " + item);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Simulate processing time
		subscription.request(1);
        latch.countDown();
	}

	@Override
	public void onError(Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void onComplete() {
		System.out.println("Processing complete.");
	}

}
