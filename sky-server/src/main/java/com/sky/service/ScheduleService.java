package com.sky.service;

/**
 * 定时任务
 */
public interface ScheduleService {
    void openStore();
    void closeStore();
    void checkAndSetStoreStatus(); // 检查并设置店铺状态
}
