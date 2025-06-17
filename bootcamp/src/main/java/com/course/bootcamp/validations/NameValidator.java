package com.course.bootcamp.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true; //handled by @NotNull
		}
		
		return value.matches("[a-zA-Z\\s-]+");
	}

}
