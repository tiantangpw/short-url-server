package com.ttpw.shorturl.service;

import cn.hutool.crypto.digest.MD5;
import com.ttpw.shorturl.aop.Log;
import com.ttpw.shorturl.exception.JsonException;
import com.ttpw.shorturl.exception.Status;
import com.ttpw.shorturl.model.ConstantValue;
import com.ttpw.shorturl.model.Shorturl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author willlee
 * @Date 2022/4/30 11:52
 **/
@Slf4j
@Service
public class ShorturlService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ShortCodeService shortCodeService;


    /**
     * 构建短链
     * @param url
     * @return
     */
    @Log
    public String buildShortUrl(String url){
        if(!StringUtils.hasText(url)||url.length()>500){
          throw new JsonException(Status.BODY_NOT_MATCH.getCode(),"url长度不对!");
        }
        return ConstantValue.short_url_prefix+fetchShortCode(url);
    }

    /**
     * 获取短码
     * @param url
     * @return
     */
    public String fetchShortCode(String url){

        if(!url.startsWith(ConstantValue.HTTP)  ){
         url+=ConstantValue.HTTP+"://";
        }
        if(!testURL(url)  ){
          throw new JsonException(Status.BODY_NOT_MATCH.getCode(),url+",不符合URL规范!");
        }
        if(!accessUrl(url)  ){
            throw new JsonException(Status.BODY_NOT_MATCH.getCode(),url+",访问异常,请确保该链接可以被访问到!");
        }
        String md5Key = MD5.create().digestHex(url);

        Shorturl shortUrl = shortCodeService.existMd5Key(md5Key);
        if(Objects.nonNull(shortUrl)){
          return shortUrl.getShortCode();
        }

        for (int i = 0; i < 5; i++) {//短码有重复使用的可能,尝试5次
            String shortcode = shortCodeService.getShortCode();
            if(Objects.isNull(shortcode)  ){
              break;//当前Redis中没有短码
            }
            String urlFromMongo = shortCodeService.getUrlFromMongo(shortcode);
            if(Objects.isNull(urlFromMongo)  ){//判断MongoDB中是否已经存在shortcode
                shortCodeService.saveShortCode(shortcode,url,md5Key);
                return shortcode;
            }
        }

        throw new JsonException(Status.SERVER_BUSY);
    }

    /**
     * 测试url是否可以访问
     * @param url
     * @return
     */
    public boolean accessUrl(String url){
        try {

            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            HttpStatus statusCode = forEntity.getStatusCode();
            log.info(url+"=="+statusCode.toString());
            if(statusCode.is2xxSuccessful()
                    ||statusCode.is1xxInformational()
                    ||statusCode.is3xxRedirection()
                    ||statusCode.is5xxServerError()

            ){
              return true;
            }else if(statusCode.is4xxClientError()||statusCode.isError()  ){
              return false;
            }
        }catch (HttpClientErrorException clientEx){//40x 客户端异常
            return false;
        }catch (HttpServerErrorException serverEx){//50x 服务端异常
            return true;

        }catch (ResourceAccessException accessEx){// 无法访问
            return false;
        }catch (Exception e) {
            log.error(url+",访问异常!",e.getMessage());
            return false;

        }
        return false;
    }

    /**
     * 校验url格式
     * @param url
     * @return
     */
    private static boolean testURL(String url){
        String regex="(http|https)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        return Pattern.matches(regex,url);

    }

    /**
     * 用短码找URL, 已在NGINX中实现
     * @param code
     * @return
     */
    @Log
    public String changeShortCode(String code){
        String errorMsg=code+",不合法!";
        if(code.length()!=6  ){
          throw new JsonException(Status.BODY_NOT_MATCH.getCode(),errorMsg);
        }
        String urlFromRedis = shortCodeService.getUrlFromRedis(code);
        if(ConstantValue.none.equals(urlFromRedis)  ){
            throw new JsonException(Status.BODY_NOT_MATCH.getCode(),errorMsg);
        }

        if(urlFromRedis!=null&&urlFromRedis.startsWith(ConstantValue.HTTP)  ){
            return urlFromRedis;
        }
        if(urlFromRedis==null  ){
            String urlFromMongo = shortCodeService.getUrlFromMongo(code);
            if(Objects.nonNull(urlFromMongo)  ){

                shortCodeService.cachedShortCode(code,urlFromMongo);
                return urlFromMongo;
            }else{
                shortCodeService.cachedShortCode(code,ConstantValue.none);
                throw new JsonException(Status.BODY_NOT_MATCH.getCode(),errorMsg);
            }
        }
        throw new JsonException(Status.BODY_NOT_MATCH.getCode(),errorMsg);
    }

}
