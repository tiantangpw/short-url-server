package com.ttpw.shorturl.service;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.ttpw.shorturl.model.ConstantValue;
import com.ttpw.shorturl.model.Shorturl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author willlee
 * @Date 2022/4/30 18:53
 **/
@Slf4j
@Service
public class ShortCodeService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SnowflakeGenerator idGenerator;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 保存短码-映射
     * @param shortcode
     * @param url
     * @param md5Key
     * @return
     */
    public boolean saveShortCode(String shortcode,String url,String md5Key){
        Shorturl shortUrl=new Shorturl();
        shortUrl.setId(idGenerator.next());
        shortUrl.setMd5Key(md5Key);
        shortUrl.setCreaterTime(LocalDateTime.now());
        shortUrl.setUrl(url);
        shortUrl.setShortCode(ConstantValue.redis_shortcode_prefix+shortcode);
        mongoTemplate.insert(shortUrl);
        cachedShortCode(shortcode, url);
        return true;
    }

    public Shorturl existMd5Key(String md5Key){
        return mongoTemplate.findOne(new Query(Criteria.where(ConstantValue.md5_key).is(md5Key)), Shorturl.class);
    }

    public String getShortCode(){
        return stringRedisTemplate.opsForSet().pop(ConstantValue.remainder_shortcode);
    }

    /**
     *  缓存短码-URL映射到Redis中
     * @param shortcode
     * @param url
     */
    public void cachedShortCode(String shortcode,String url){
        stringRedisTemplate.opsForValue().set(ConstantValue.redis_shortcode_prefix+shortcode,url,30, TimeUnit.DAYS);
    }
    public String getUrlFromRedis(String code){
        return stringRedisTemplate.opsForValue().get(ConstantValue.redis_shortcode_prefix+code);
    }

    public String getUrlFromMongo(String code){
        Shorturl shorturl=mongoTemplate.findOne(new Query(Criteria.where(ConstantValue.short_code).is(ConstantValue.redis_shortcode_prefix+code)), Shorturl.class);
        if(Objects.nonNull(shorturl)){
          return shorturl.getUrl();
        }

        return null;
    }

    /**
     * Redis中剩余的短码
     * @return
     */
    public Long remainderShortcode(){
        Long size = stringRedisTemplate.opsForSet().size(ConstantValue.remainder_shortcode);
        return size==null?0:size;
    }

    /**
     * 批量添加短码到Redis中
     * @param codes
     * @return
     */
    public boolean batchSaveCode(Set<String> codes){

        Long add = stringRedisTemplate.opsForSet().add(ConstantValue.remainder_shortcode, codes.toArray(new String[codes.size()]));
        return add!=null?true:false;
    }
}
