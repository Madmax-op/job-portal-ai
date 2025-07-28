package com.vivek.spring_boot_rest.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // return type , class-name.method-name(args)
    @Before("execution(* com.vivek.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodCall (JoinPoint jp){
        LOGGER.info("Method Called"+ jp.getSignature().getName());
    }

    @After("execution(* com.vivek.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodCallExecuted(JoinPoint jp){
        LOGGER.info("Method Executed" + jp.getSignature().getName());
    }

    @AfterThrowing("execution(* com.vivek.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodCallCrash(JoinPoint jp){
        LOGGER.info("Method has some issues" + jp.getSignature().getName());
    }
}
