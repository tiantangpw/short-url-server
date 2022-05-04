package com.ttpw.shorturl.ctrl;

import com.google.common.util.concurrent.RateLimiter;
import com.ttpw.shorturl.aop.RateLimit;
import com.ttpw.shorturl.exception.ApiResponse;
import com.ttpw.shorturl.exception.Status;
import com.ttpw.shorturl.service.ShorturlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author willlee
 * @Date 2022/5/3 9:41
 **/
@RestController
public class ShortUrlController {

    @Autowired
    private ShorturlService shorturlService;


    @RateLimit
    @GetMapping("/url/buildshort")
    public String buildShortUrl(@RequestParam("url") String url){
        return shorturlService.buildShortUrl(url);
    }
}
