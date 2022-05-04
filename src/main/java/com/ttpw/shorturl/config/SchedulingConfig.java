package com.ttpw.shorturl.config;

import com.ttpw.shorturl.service.ShortCodeService;
import com.ttpw.shorturl.utils.ShortCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

/**
 * 定时任务配置
 * @author willlee
 *
 */
@Slf4j
@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Autowired
    private ShortCodeService shortCodeService;

    /**
     * 批量生产短码保存到Redis中
     */
    @Scheduled(cron = "1 0 * * * *") // 每小时执行一次
    public void bacthSave() {
        try {
			log.info("定时任务开始.......");
            Long aLong = shortCodeService.remainderShortcode();
            if(aLong<10001 ){
                Set<String> codes = ShortCodeUtil.getCodes();
                shortCodeService.batchSaveCode(codes);
                codes=null;
            }
        } catch (Exception e) {
			log.error("定时任务出错",e);
		}
    }
}