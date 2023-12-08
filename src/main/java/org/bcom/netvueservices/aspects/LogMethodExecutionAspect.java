package org.bcom.netvueservices.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogMethodExecutionAspect {
    @Around("@annotation(org.bcom.netvueservices.annotations.LogMethodExecution)")
    public Object logMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String methodName = methodSignature.getName();

        log.debug("---- Start: " + methodName + " ----");
        Object result = proceedingJoinPoint.proceed();
        log.debug("---- End: " + methodName + " ----");
        return result;
    }
}
