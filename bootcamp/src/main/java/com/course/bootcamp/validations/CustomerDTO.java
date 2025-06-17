package com.course.bootcamp.validations;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerDTO(
		@NotNull
		@ValidName
		String name, 
		@Email String email,
		@Valid List<@NotNull OrderDTO> orders) {

}
