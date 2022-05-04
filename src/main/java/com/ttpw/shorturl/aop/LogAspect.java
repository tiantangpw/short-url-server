package com.ttpw.shorturl.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 控制器日志切面
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class LogAspect {

	private double stime;

    private String className;
	private String methodName;

	@Pointcut("@annotation(com.ttpw.shorturl.aop.Log)")
	public void log(){}

	/**
	 * 进入controller方法前，输出参数
	 * @param joinPoint
	 */
	@Before("log()")
	public void befor(JoinPoint joinPoint) throws Exception{
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        
        String classType = joinPoint.getTarget().getClass().getName();    
        Class<?> clazz = Class.forName(classType);
        synchronized (clazz) {
        	 className = clazz.getSimpleName();
             
             methodName = joinPoint.getSignature().getName();
		}
        
        //参数
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        Class<?>[] paramTypes = ((CodeSignature)joinPoint.getSignature()).getParameterTypes();
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<paramValues.length; i++){
        	sb.append("\t").append(paramNames[i]).append(" : ")
                    .append(paramValues[i]).append("\t(")
                    .append(paramTypes[i].getSimpleName()).append(")")
                    .append("\n");
        }

        log.info("\n\n className :\t{}\n methodName :\t{}\n action :\t{}\n params :{\n{} }\n"
        		, className, methodName, request.getMethod(), sb);
        stime = System.currentTimeMillis();
	}
	
    @After("log()")
    public void doAfter(){
       ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
       assert attributes != null;
       HttpServletRequest request = attributes.getRequest();
       log.info("url = {} end of execution", request.getRequestURL());
    }
    
    @AfterThrowing("log()")
    public void throwss(){
    	log.error("throwss");
    }

    /**
     * 获取返回数据，记录时长
     * @param object 返回数据
     */
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturn(Object object){
        if(object instanceof List) {
            int returnCount = 5;
            if(((List)object).size() <= returnCount) {
                log.debug("response size : {}",object);
            } else {
                log.debug("response five size : {}", ((List)object).subList(0,5));
            }
        } else {
            log.debug("response : {}",object);
        }
        double etime = System.currentTimeMillis();
  	    log.info("{}.{} time consuming: "+((etime -stime)/1000)+"s  \n", className, methodName);
    }
}
