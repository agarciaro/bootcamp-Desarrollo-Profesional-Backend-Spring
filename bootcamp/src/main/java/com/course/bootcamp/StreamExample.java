package com.course.bootcamp;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.course.bootcamp.model.Customer;
import com.course.bootcamp.model.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamExample {

	public static void main(String[] args) {
		//streamExample1();
		//streamExample2();
		streamExample3();
	}

	public static void streamExample1() {
		// Example of using Java Streams to process a list of integers
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		numbers.parallelStream()
			.filter(n -> n % 2 == 0) // Filter even numbers
			.map(n -> {
				log.info("Processing number: {}", n);
				return n * n;
			}) // Square each number
			.collect(Collectors.toList());
	}
	
	
	// Filter a list of customers to include only those aged 25 or older 
	// and transform their emails to uppercase, sorted alphabetically.
	public static void streamExample2() {
		List<Customer> customers = List.of(
			new Customer(UUID.randomUUID(), "John Doe", "john@example.com", 30),
		    new Customer(UUID.randomUUID(), "Jane Smith", "jane@example.com", 22),
		    new Customer(UUID.randomUUID(), "Alice Brown", "alice@example.com", 28),
		    new Customer(UUID.randomUUID(), "Bob Wilson", null, 25)
		);
		
		List<String> emails = customers.stream()
			.filter(customer -> customer.age() >= 25)
			.map(Customer::email)
			.filter(email -> email != null)
			.map(String::toUpperCase)
			.sorted()
			.toList();
		
		log.info("emails:{}", emails);
	}
	
	// Group a list of orders by their status and calculate the total amount for each status, 
	// returning a map of status to total amount.
	public static void streamExample3() {
		List<Order> orders = List.of(
		    new Order(UUID.randomUUID(), UUID.randomUUID(), 100.0, "PENDING"),
		    new Order(UUID.randomUUID(), UUID.randomUUID(), 200.0, "COMPLETED"),
		    new Order(UUID.randomUUID(), UUID.randomUUID(), 150.0, "PENDING"),
		    new Order(UUID.randomUUID(), UUID.randomUUID(), null, "COMPLETED")
		);
		
		Map<String, DoubleSummaryStatistics> groupedOrders = orders.stream()
			.filter(order -> order != null && order.status() != null)
			.collect(Collectors.groupingBy(
				Order::status,
				Collectors.summarizingDouble(order -> order.amount() != null ? order.amount() : 0.0)
			));
		
		log.info("Orders Grouped: {}", groupedOrders);
			
	}
}