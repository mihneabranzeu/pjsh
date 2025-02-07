package com.example.eventplanning.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.eventplanning.service.EventService.signUpToEvent(..))")
    public void signUpToEvent() {}


    @Around("signUpToEvent()")
    public Object logSignUpToEvent(ProceedingJoinPoint joinPoint) throws Throwable{
        Object[] methodArgs = joinPoint.getArgs();
        Long eventId = (Long) methodArgs[0];
        String username = (String) methodArgs[1];
        logger.info("User {} is signing up to event with id {}", username, eventId);

       Object result;

        try {
            result = joinPoint.proceed();
            logger.info("User {} has signed up to event with id {}", username, eventId);
        } catch (Exception e) {
            logger.error("User {} failed to sign up to event with id {}", username, eventId);
            throw e;
        }

        return result;
    }
}
