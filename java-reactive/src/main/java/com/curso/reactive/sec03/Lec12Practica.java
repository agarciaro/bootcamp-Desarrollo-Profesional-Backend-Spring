package com.curso.reactive.sec03;

import com.curso.reactive.common.ExternalServiceClient;
import com.curso.reactive.common.Util;
import com.curso.reactive.sec03.practica.StockPriceObserver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec12Practica {

	public static void main(String[] args) {
		
		
		var client = new ExternalServiceClient();
		var subscriber = new StockPriceObserver();
		client.getPriceChanges().subscribe(subscriber);
		
		Util.sleepSeconds(21);
		log.info("Proceso finalizado");
		
	}

}
