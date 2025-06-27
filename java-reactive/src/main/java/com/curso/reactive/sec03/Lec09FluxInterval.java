package com.curso.reactive.sec03;

import com.curso.reactive.common.ExternalServiceClient;
import com.curso.reactive.common.Util;
import com.curso.reactive.sec01.subscriber.SubscriberImpl;
import com.curso.reactive.sec03.helper.NameGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec09FluxInterval {

	public static void main(String[] args) {

		var client = new ExternalServiceClient();
		
		client.getPriceChanges()
			.subscribe(Util.createSubscriber("sub1"));
		
		client.getPriceChanges()
			.subscribe(Util.createSubscriber("sub2"));
		
		Util.sleepSeconds(6);

	}

}
