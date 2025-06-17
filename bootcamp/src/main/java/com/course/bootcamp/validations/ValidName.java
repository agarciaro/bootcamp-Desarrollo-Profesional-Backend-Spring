package com.course.bootcamp.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = NameValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface ValidName {
	
	String message() default "Name can only contain only letter, spaces and hyphens";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}
