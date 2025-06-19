package com.bootcamp.first.microservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3972098019944742957L;
	
	@Getter
	private int userId;
	
}
