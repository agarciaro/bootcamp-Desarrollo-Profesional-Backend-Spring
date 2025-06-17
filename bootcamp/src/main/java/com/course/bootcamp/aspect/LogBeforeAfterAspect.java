package com.course.bootcamp.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogBeforeAfterAspect {
	
	@Before("execution(* *.go(..))")
	public void logBefore() {
		try {
			System.out.println("Before method execution: Logging aspect triggered.");
		} catch (Exception e) {
			System.err.println("An error occurred in the logging aspect: " + e.getMessage());
		}
	}
	
	@After("execution(* *.set*(..))")
	public void logAfter() {
		System.out.println("After method execution: Logging aspect triggered.");
	}
	
}
