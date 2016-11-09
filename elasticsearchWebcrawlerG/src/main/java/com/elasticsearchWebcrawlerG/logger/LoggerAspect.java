package com.elasticsearchWebcrawlerG.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggerAspect {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	String name = "";
	String type = "";
	
	//@Around("execution(* elasticsearchWebcrawlerG..controller.*Controller.*(..)) or execution(* elasticsearchWebcrawlerG..service.*Service.*(..))")
	@Around("execution(* com.elasticsearchWebcrawlerG.service.*.*(..)")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		
		type = joinPoint.getSignature().getDeclaringTypeName();
		
		if (type.indexOf("Controller") > -1) {
			name = "Controller  \t:  ";
		}
		else if (type.indexOf("Service") > -1) {
			name = "Service  \t:  ";
		}
		
		log.debug(name + type + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
