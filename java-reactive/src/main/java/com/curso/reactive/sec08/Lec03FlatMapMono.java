package com.curso.reactive.sec08;

import com.curso.reactive.common.Util;
import com.curso.reactive.sec08.helper.OrderService;
import com.curso.reactive.sec08.helper.UserService;

public class Lec03FlatMapMono {
	
	public static void main(String[] args) {
		//Tenemos username
		//Queremos account balance
		
//		UserService.getUserId("sam")
//			.flatMap(PaymentService::getUserBalance)
//			.subscribe(Util.createSubscriber());
		
		//Tenemos username
		//Queremos todos los pedidos de ese user
		
		UserService.getUserId("mike")
			.flatMapMany(OrderService::getUserOrders)
			.subscribe(Util.createSubscriber());
		
		Util.sleepSeconds(3);
	}
	
}
