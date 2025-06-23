package com.talan.restaurant.shared.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.talan.restaurant.shared.dto.ApiError;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorController {
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleBadRequest(MethodArgumentNotValidException exception) {
		return ApiError.builder()
				.code(400)
				.message(exception.getAllErrors()
						.stream()
						.map(error -> error.getObjectName() + "-" + error.getDefaultMessage())
						.toList().toString()
				).build();
	}
	
	@ExceptionHandler({UnsupportedOperationException.class, Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiError handleUncaughtException(Exception exception) {
		return ApiError.builder()
				.code(500)
				.message("Uncaught Error: " + exception.getMessage())
				.build();
	}
	
}
