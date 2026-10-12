package com.guhecloud.cube.biz.daily.task.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 计划
 */
@FeignClient(name = "dailyTask")
public interface DailyPlanClient {

}
