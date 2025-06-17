package com.course.bootcamp.model;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class Restaurant {
	
	@Getter
	//@Setter
	private final String name = "The Best Restaurant";
	
	public void go() {
		System.out.println("Restaurant is open for business!");
	}
	
}
