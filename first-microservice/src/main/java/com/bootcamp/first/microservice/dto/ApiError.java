package com.bootcamp.first.microservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
	
	private int code;
	private String message;
	
}
