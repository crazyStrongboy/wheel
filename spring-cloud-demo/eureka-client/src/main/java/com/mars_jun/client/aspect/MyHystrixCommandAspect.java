package com.mars_jun.client.aspect;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author hejun
 * @date 2019/04/19
 */
@Aspect
@Component
public class MyHystrixCommandAspect {

    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Pointcut(value = "@annotation(MyHystrixCommand)")
    public void pointCut() {

    }

    @Around(value = "pointCut()&&@annotation(hystrixCommand)")
    public Object doPointCut(ProceedingJoinPoint joinPoint, MyHystrixCommand hystrixCommand) throws Throwable {
        int timeout = hystrixCommand.value();
        Future future = executor.submit(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
            }
            return null;
        });
        Object returnValue = null;
        try {
            returnValue = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            future.cancel(true);
            if (StringUtils.isBlank(hystrixCommand.fallback())){
                throw e;
            }
            returnValue = invokeFallbackMethod(joinPoint, hystrixCommand.fallback());
        }
        return returnValue;
    }

    private Object invokeFallbackMethod(ProceedingJoinPoint joinPoint, String fallback) throws Exception {
        Method method = findFallbackMethod(joinPoint, fallback);
        if (method == null) {
            throw new Exception("can not find fallback :" + fallback + " method");
        } else {
            method.setAccessible(true);
            try {
                Object invoke = method.invoke(joinPoint.getTarget(), joinPoint.getArgs());
                return invoke;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw e;
            }
        }
    }


    private Method findFallbackMethod(ProceedingJoinPoint joinPoint, String fallbackMethodName) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method fallbackMethod = null;
        try {
            fallbackMethod = joinPoint.getTarget().getClass().getMethod(fallbackMethodName, parameterTypes);
        } catch (NoSuchMethodException e) {
        }
        return fallbackMethod;
    }

}
