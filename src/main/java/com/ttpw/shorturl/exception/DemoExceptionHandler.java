package com.ttpw.shorturl.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
/**
 *
 * 全局异常处理只能处理 Controller层面的异常
 * 而对于mapper service引起的异常 要么往controller 层抛
 * 要么就 try-catch 捕获 自己处理
 */
@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {
	private static final String DEFAULT_ERROR_VIEW = "error";

	/**
	 * 统一 json 异常处理
	 *
	 * @param exception JsonException
	 * @return 统一返回 json 格式
	 */
	@ExceptionHandler(value = JsonException.class)
	@ResponseBody
	public ApiResponse jsonErrorHandler(HttpServletRequest req, HandlerMethod method, JsonException exception) {
        log.error(String.format("访问 %s -> %s 出现业务异常！", req.getRequestURI(), method.toString()), exception);
        return ApiResponse.ofException(exception);
	}

	/**
	 * 统一 页面 异常处理
	 *
	 * @param exception PageException
	 * @return 统一跳转到异常页面
	 */
	@ExceptionHandler(value = PageException.class)
	public ModelAndView pageErrorHandler(PageException exception) {
		log.error("【DemoPageException】:{}", exception.getMessage());
		ModelAndView view = new ModelAndView();
		view.addObject("code", exception.getCode());
		view.addObject("message", exception.getMessage());
		view.setViewName(DEFAULT_ERROR_VIEW);//跳转到error.html
		return view;
	}

    /**
     * 处理空指针的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ApiResponse exceptionHandler(NullPointerException e){
        String err="发生空指针异常！";
        log.error(err,e);
        return ApiResponse.of(Status.UNKNOWN_ERROR.getCode(),err,null);
    }

    /**
     * 一般性异常,兜底处理
     * @param req
     * @param method
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResponse exceptionHandler(HttpServletRequest req, HandlerMethod method, Exception ex) {
        //输出一下请求参数
        log.info("请求参数：" + req.getQueryString());
        log.error(String.format("访问 %s -> %s 出现系统异常！", req.getRequestURI(), method.toString()), ex);
        return ApiResponse.of(Status.UNKNOWN_ERROR.getCode(), "服务器繁忙", null);
    }
}