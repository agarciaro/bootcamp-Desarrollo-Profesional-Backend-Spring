package com.curso.reactive.sec03;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec01.subscriber.SubscriberImpl;
import com.curso.reactive.sec03.helper.NameGenerator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class Lec07FluxVsList {

	public static void main(String[] args) {

//		var list = NameGenerator.getNamesList(10);
//		log.info("{}",list);
		
		var subscriber = new SubscriberImpl();
		NameGenerator.getNamesFlux(10)
			.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().cancel();
		subscriber.getSubscription().request(3);

	}

}
