package com.ttpw.shorturl.service;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ttpw.shorturl.model.Shorturl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author willlee
 * @Date 2022/4/30 11:52
 **/
@Service
public class ShorturlService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SnowflakeGenerator idGenerator;


    public boolean insert(){
        Shorturl url=new Shorturl();
        url.setId(idGenerator.next());
        url.setShortCode("sdfrew");
        url.setUrl("http://www.baidu.com");
        url.setCreaterTime(LocalDateTime.now());
        Shorturl shorturl = mongoTemplate.insert(url);
        System.out.println(shorturl.toString());
        return shorturl==null?false:true;
    }
}
