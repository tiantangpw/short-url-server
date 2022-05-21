package com.ttpw.shorturl.ctrl;

import com.ttpw.shorturl.aop.RateLimit;
import com.ttpw.shorturl.service.ShorturlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView buildShortUrl(@RequestParam("url") String url){
        String shortUrl = shorturlService.buildShortUrl(url);
        ModelAndView view = new ModelAndView();
        view.addObject("code", "");
        view.addObject("message", shortUrl);
        view.setViewName("result");
        return view;

    }
}
