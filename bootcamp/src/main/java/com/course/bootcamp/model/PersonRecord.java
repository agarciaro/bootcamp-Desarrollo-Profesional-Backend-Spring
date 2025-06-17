package com.course.bootcamp.model;

import jakarta.validation.constraints.NotNull;

public record PersonRecord(
		@NotNull String name, 
		String email, 
		String phone, 
		String address) {
	

}
