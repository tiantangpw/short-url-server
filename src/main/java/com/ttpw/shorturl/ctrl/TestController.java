package com.ttpw.shorturl.ctrl;

import com.ttpw.shorturl.exception.ApiResponse;
import com.ttpw.shorturl.exception.JsonException;
import com.ttpw.shorturl.exception.PageException;
import com.ttpw.shorturl.exception.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class TestController {

    @GetMapping("hello")
    @ResponseBody
    public ApiResponse hello() {
    	// 正常返回结果 我们就不需要抛出异常了 直接用 ApiResponse
        return ApiResponse.ofStatus(Status.OK,"hello bb");
    }
    @GetMapping("hello2")
    @ResponseBody
    public ApiResponse hello2() {
    	// 直接返回不了解的错误 json格式
        throw new JsonException(Status.UNKNOWN_ERROR);
    }
    @GetMapping("hello3")
    //不需要加@ResponseBody ,非常需要注意的一点 如果你返回的是模板页面 那么需要引入thymeleaf依赖 且不需要加@ResponseBody
    public String hello3() {
    	//返回的是静态页面error.html
        throw new PageException(Status.UNKNOWN_ERROR);
    }
}
