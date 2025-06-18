package com.bootcamp.first.microservice.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
	
	@Getter
	private String problem;
	
	public UserNotFoundException(String problem) {
		this.problem = problem;
	}
	
}
