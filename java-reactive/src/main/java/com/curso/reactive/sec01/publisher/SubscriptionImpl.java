package com.curso.reactive.sec01.publisher;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriptionImpl implements Subscription {
	
	private static final int MAX_ITEMS = 10;
	
	private final Faker faker;
	private final Subscriber<? super String> subscriber;
	private boolean isCancelled;
	private int count = 0;
	
	
	public SubscriptionImpl(Subscriber<? super String> subscriber) {
		this.subscriber = subscriber;
		this.faker = Faker.instance();
	}
	
	@Override
	public void request(long requested) {
		if (isCancelled || requested <= 0) {
			return;
		}
		
		log.info("subscriber has requested {} items", requested);
		if (requested > MAX_ITEMS) {
			this.subscriber.onError(new RuntimeException("Validation error: Requested items exceed maximum limit of " + MAX_ITEMS));
			this.isCancelled = true;
			return;
		}
		
		for (int i = 0; i < requested && count < MAX_ITEMS; i++) {
            count++;
            this.subscriber.onNext(this.faker.chuckNorris().fact());
            
        }
		
		if(count == MAX_ITEMS){
            log.info("no more data to produce");
            this.subscriber.onComplete();
            this.isCancelled = true;
        }
		
		
	}

	@Override
	public void cancel() {
		log.info("subscription cancelled");
		this.isCancelled = true;
	}

}
