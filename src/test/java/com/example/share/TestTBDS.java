package com.example.share;

import com.ttpw.ShareApplication;
import com.ttpw.shorturl.service.ShorturlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

/**
 * @Author willlee
 * @Date 2022/4/2 10:24
 **/
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShareApplication.class)
public class TestTBDS {

    @Autowired
    RestTemplate restTemplate;
    @Autowired ShorturlService shorturlService;



    private static boolean testURL(String url){
        String regex="(http|https)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        return Pattern.matches(regex,url);

    }
    @Test
    public void getWWW(){
        try {
//            String url="http://www.o.com";
//            String url="https://fanyi.baidu.com/#zh/en/%E5%A6%82%E6%9E%9C%E4%B8%80%E4%B8%AA%E6%A8%A1%E5%BC%8F%E8%A6%81%E8%A2%AB%E5%A4%9A%E6%AC%A1%E4%BD%BF%E7%94%A8%EF%BC%8C%E9%82%A3%E4%B9%88%E7%BC%96%E8%AF%91%E4%B8%80%E6%AC%A1%E5%B9%B6%E9%87%8D%E7%94%A8%E5%AE%83%E5%B0%86%E6%AF%94%E6%AF%8F%E6%AC%A1%E8%B0%83%E7%94%A8%E8%BF%99%E4%B8%AA%E6%96%B9%E6%B3%95%E6%9B%B4%E6%9C%89%E6%95%88%E3%80%82";
//            String url="https://fanyi.baidu.com/#en/zh/If%20a%20pattern%20is%20to%20be%20used%20multiple%20times%2C%20compiling%20it%20once%20and%20reusing%20it%20will%20be%20more%20efficient%20than%20invoking%20this%20method%20each%20time.";
//              String url="http://cache.baiducontent.com/c?m=EbDM23jIscyr7mRuUqgUdboY4G3wYtbBQgLd--vOWlP7ba0mZOXeg9g06x8gZtKA27XC4ZUarUYGnSXU_FTxK6-DlDNOcIUXdez2kn6a7KuBelkp1804gGdbE---qqheaF4vTtd2YyMj5p_he70tGIcFu7Cl2ZvUL0AbJlHhEuYihuZJQt3YJ3I4Vjk7h7yj2IaREqhZb3EGQmhpflKpzgoePryEM7zNdpQ89Kpar5_QB8FmpzEOr7QLgV9yI0yjb9s5edgdiRH-6divwmTvqR67BmeFHUu50ANeMZDXBv3&p=90769a47839c11a05be98f645c5d&newp=c6769a47cdd25fb700bd9b7d095c92695912c10e3fd5d201298ffe0cc4241a1a1a3aecbb24261a02d4c27f630abb0f31aba7747d605f76ac8ad69012&s=cfcd208495d565ef&user=baidu&fm=sc&query=%D5%FB%B8%F6%CE%F7%B7%BD%B6%BC%CE%AA%C2%ED%BF%CB%C1%FA%C4%F3%D2%BB%B0%D1%BA%B9&qid=ecacf494000067c5&p1=6";
//              String url="https://www.baidu.com/s?wd=%E6%95%B4%E4%B8%AA%E8%A5%BF%E6%96%B9%E9%83%BD%E4%B8%BA%E9%A9%AC%E5%85%8B%E9%BE%99%E6%8D%8F%E4%B8%80%E6%8A%8A%E6%B1%97&sa=fyb_n_homepage&rsv_dl=fyb_n_homepage&from=super&cl=3&tn=baidutop10&fr=top1000&rsv_idx=2&hisfilter=1";
//            String url="https://search.yahoo.co.jp/search?p=tokyo+hot&fr=top_ga1_sa&ei=UTF-8&ts=39065&aq=-1&oq=&at=&ai=8475b2fc-6e1e-4664-b991-38c9c3a1fe6f";
//            String url="http://www.u8.work/adsfdaf";
//            String url="https://www.jianshu.com/p/6455e25b568f";
            String url="https://blog.csdn.net/woshiduxingjun/article/details/118602752";
//            String url="https://blog.csdn.net/woshiduxingjun/article/details/118602752?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522165076985216782350982769%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=165076985216782350982769&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-6-118602752.142^v9^control,157^v4^control&utm_term=resttemplate%E8%8E%B7%E5%8F%96%E5%93%8D%E5%BA%94%E5%A4%B4&spm=1018.2226.3001";
            if(!testURL(url)  ){

                System.out.println("非法url "+url);
                return;
            }

            ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
            System.out.println(forEntity.getStatusCodeValue());
        }catch (HttpClientErrorException clientEx){//40x 客户端异常
            HttpStatus statusCode = clientEx.getStatusCode();
            System.out.println(statusCode);

        }
        catch (ResourceAccessException accessEx){// 无法访问
            System.out.println(accessEx.getLocalizedMessage());
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
    @Test
    public void testSave(){
        boolean insert = shorturlService.insert();
        System.out.println(insert);
    }
}
