package com.curso.reactive.sec03.practica;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockPriceObserver implements Subscriber<Integer> {
	
	private int quantity = 0;
	private int balance = 1000;
	private Subscription subscription;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		subscription.request(Long.MAX_VALUE);
		this.subscription = subscription;
	}

	@Override
	public void onNext(Integer price) {
		if(price < 95 && balance >= price) {
			quantity++;
			balance = balance - price;
			log.info("Bought a stock at {}. Total quantity:{}, remaining balance:{}", price, quantity, balance);
		} else if(price > 115 && quantity > 0) {
			subscription.cancel();
			log.info("Selling {} quantities at {}", quantity, price);
			balance = balance + (quantity * price);
			quantity = 0;
			log.info("Profit: {}", (balance - 1000));
		}
		
	}

	@Override
	public void onError(Throwable t) {
		log.error("error", t);
		
	}

	@Override
	public void onComplete() {
		log.info("Completado. Total quantity:{}, remaining balance:{}", quantity, balance);
	}
	
}
