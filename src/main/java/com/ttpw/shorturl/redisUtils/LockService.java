package com.ttpw.shorturl.redisUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class LockService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取分布式锁超时时间 单位毫秒
     */
    private static final int TIME_OUT = 60000;
    /**
     * 获取分布式锁等待间隔 单位秒
     */
    private static final int TRY_INTERVAL = 3;
    /**
     * 分布式锁key过期时间 单位秒
     */
    private static final int EXPIRE_TIME = 60;

    /**
     * 分布式锁加锁自定义名称
     */
    public boolean tryLock(String uuid, String lockName) {
        return tryLock(uuid, lockName, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * @param lockName, time, unit
     * @param uuid
     * @return boolean
     * @author makaiyu
     * @description 分布式锁加锁
     * @date 10:51 2019/8/8
     **/
    public boolean tryLock(String uuid, String lockName, long time, TimeUnit unit) {
        try {
            long startTime = System.currentTimeMillis();
            // 判断锁是否过期
            while (System.currentTimeMillis() - startTime < TIME_OUT) {
                // 是否获取到锁，返回true获取到锁，如果获取不到就等待3秒重新调用 setIfAbsent就是setnx的意思
                if (stringRedisTemplate.opsForValue().setIfAbsent(lockName, uuid)) {
                    // 为锁加过期时间，防止死锁出现
                    Boolean flag = stringRedisTemplate.expire(lockName, time, unit);
                    log.debug("all-frontier 加锁成功 : {} ,key : {}", flag, lockName);
                    // 如果给锁添加时间失败，就直接删除锁
                    if (!flag) {
                        unlock(uuid, lockName);
                    }
                    return flag;
                }
                Thread.sleep(TRY_INTERVAL);
            }
            return false;
        } catch (Exception e) {
            // 如果出现异常情况 删除锁
            unlock(uuid, lockName);
            log.debug("all-frontier 出现异常情况 删除锁 lockName:{}", lockName);
            return false;
        }
    }

    public void unlock(String uuid, String lockName) {
        String result = stringRedisTemplate.opsForValue().get(lockName);
        if (uuid.equals(result)) {
            stringRedisTemplate.delete(lockName);
        }
    }

}
