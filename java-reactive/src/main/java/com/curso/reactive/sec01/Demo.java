package com.curso.reactive.sec01;

import com.curso.reactive.sec01.publisher.PublisherImpl;
import com.curso.reactive.sec01.subscriber.SubscriberImpl;

public class Demo {

	public static void main(String[] args) throws InterruptedException {
		demo4();
	}
	
	private static void demo1() {
		var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
	}
	
	private static void demo2() throws InterruptedException {
		var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
        subscriber.getSubscription().request(1);
        Thread.sleep(2000);
        subscriber.getSubscription().request(5);
        Thread.sleep(2000);
	}
	
	private static void demo3() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(1);
        Thread.sleep(2000);
        subscriber.getSubscription().cancel();
        subscriber.getSubscription().request(3);
        Thread.sleep(2000);
	}
	
	private static void demo4() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(9);
        Thread.sleep(2000);
        subscriber.getSubscription().request(5);
        Thread.sleep(2000);
	}
	
}
