package com.sky.service.impl;

import com.sky.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/*
定时任务类。实现定时任务。
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /*
     * 设置每天早上九点定时开启店铺
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void openStore() {
        log.info("准备设置店铺状态为开启");
        redisTemplate.opsForValue().set("SHOP_STATUS", 1);
    }

    @Scheduled(cron = "0 30 11 * * ?")
    public void closeStore() {
        log.info("准备设置店铺状态为关闭");
        redisTemplate.opsForValue().set("SHOP_STATUS", 0);
    }

    @Override
    public void checkAndSetStoreStatus() {
        LocalTime now= LocalTime.now();
        LocalTime openTime = LocalTime.of(9, 0); // 9:00
        LocalTime closeTime = LocalTime.of(11, 30); // 11:30

        if(now.isAfter(openTime) && now.isBefore(closeTime)){
            openStore();
        }
        else{
            closeStore();
        }
    }

}
