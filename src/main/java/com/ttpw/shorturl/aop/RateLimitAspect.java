package com.ttpw.shorturl.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.ttpw.shorturl.exception.ApiResponse;
import com.ttpw.shorturl.exception.Status;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 */
@Component
@Scope
@Aspect
@Slf4j
public class RateLimitAspect {
    //每秒只发出3个令牌,内部采用令牌捅算法实现
    private static RateLimiter rateLimiter = RateLimiter.create(50.0);//一秒允许50次

    /**
     * 业务层切点
     */
    @Pointcut("@annotation(com.ttpw.shorturl.aop.RateLimit)")
    public void ServiceAspect() { }

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {

        try {
            //tryAcquire()是非阻塞, rateLimiter.acquire()是阻塞的
            if (rateLimiter.tryAcquire(1, TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                //拒绝了请求
                return ApiResponse.ofStatus(Status.SERVER_BUSY).toString();
            }
        } catch (Throwable e) {
            log.error("",e);
            return ApiResponse.ofStatus(Status.UNKNOWN_ERROR).toString();
        }

    }
}
