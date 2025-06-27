package com.curso.reactive.sec01.subscriber;



import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriberImpl implements Subscriber<String>{
	
	@Getter
	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
	}

	@Override
	public void onNext(String item) {
		log.info("received item: {}", item);
	}

	@Override
	public void onError(Throwable throwable) {
		log.error("error occurred: {}", throwable.getMessage());
	}

	@Override
	public void onComplete() {
		log.info("Done receiving all items!");
	}
}
