package com.guhecloud.cube.biz.daily.task.sdk;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "dailyTask")
public interface DailySdkPlanClient {

}
