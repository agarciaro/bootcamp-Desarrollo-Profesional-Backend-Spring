package com.curso.reactive.sec05;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec05.practica.ExternalServiceClient;

public class Lec06Practica {

	public static void main(String[] args) {
		var client = new ExternalServiceClient();
		
		for (int i = 1; i < 5; i++) {
			client.getProductName(i)
				.subscribe(Util.createSubscriber());
		}
		
		Util.sleepSeconds(10);
	}

}
