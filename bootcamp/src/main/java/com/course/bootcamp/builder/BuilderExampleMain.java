package com.course.bootcamp.builder;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BuilderExampleMain {

	public static void main(String[] args) {
		/*CustomerDTO customerDTO = new CustomerDTO.Builder()
				.id(UUID.randomUUID())
				.name("Sam")
				.email("sam@email.com")
				.build();
		*/
		
		CustomerDTO customerDTO = CustomerDTO.builder()
				.id(UUID.randomUUID())
				.name("Sam")
				.email("sam@email.com")
				.build();
		
		log.info("Customer: {}", customerDTO);
		
	}

}
