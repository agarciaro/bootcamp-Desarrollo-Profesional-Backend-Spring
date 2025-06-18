package com.bootcamp.first.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/first-controller")
@Slf4j
public class FirstController {
	//@GetMapping -> Read-only requests
	//@PostMapping -> Creates 
	//@DeleteMapping -> Removes
	//@PutMapping -> Updates
	
	
	
	@GetMapping(path = "hello/{myName}/{lastName}")
	public String sayHello(@PathVariable(name = "myName") String name, @PathVariable String lastName) {
		return "Hello " + name + " " + lastName;
	}
	
	@GetMapping("home")
	public String home() {
		return "Home";
	}
	

	
	
}
