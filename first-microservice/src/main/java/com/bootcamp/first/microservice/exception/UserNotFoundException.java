package com.bootcamp.first.microservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -1801449136092128094L;
	
	@Getter
	private int userId;
		
}
