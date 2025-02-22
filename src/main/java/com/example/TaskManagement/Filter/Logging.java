package com.example.TaskManagement.Filter;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class Logging {


    @Pointcut("@annotation(com.example.TaskManagement.Filter.Loggable)")
    public void loggablemethods(){}

    @Before("loggablemethods()")
    public void logBefore(JoinPoint point){

        String method = point.getSignature().getName();
        System.out.println("Method Called"+method);
        Object[] param = point.getArgs();
        System.out.println(param);
    }

}
