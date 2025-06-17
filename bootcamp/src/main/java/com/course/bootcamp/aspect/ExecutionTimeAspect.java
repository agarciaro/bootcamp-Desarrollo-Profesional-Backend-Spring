package com.course.bootcamp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class ExecutionTimeAspect {
	
	@Around("execution(* com.course.bootcamp.*.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		Object proceedResult = joinPoint.proceed();

		long executionTime = System.currentTimeMillis() - start;

		log.info("Method {} executed in {} ms", joinPoint.getSignature(), executionTime);

		return proceedResult;
	}
	
	// var a = sum(x, y);
	
}
