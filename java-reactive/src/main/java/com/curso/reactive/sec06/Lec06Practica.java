package com.curso.reactive.sec06;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec06.practica.ExternalServiceClient;
import com.curso.reactive.sec06.practica.InventoryService;
import com.curso.reactive.sec06.practica.RevenueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Lec06Practica {
	
	public static void main(String[] args) {
		var client = new ExternalServiceClient();
		var inventoryService = new InventoryService();
		var revenueService = new RevenueService();
		
		client.orderStream().subscribe(inventoryService::consume);
		client.orderStream().subscribe(revenueService::consume);
		
		inventoryService.stream()
			.subscribe(Util.createSubscriber("inventory"));
		
		revenueService.stream()
		.subscribe(Util.createSubscriber("revenue"));
		
		Util.sleepSeconds(30);
		
	}
	
}
