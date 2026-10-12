package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("maintain")
public interface UpkeepClient {
    //设备任务是否有效
    @GetMapping("/isUpkeepValid")
    Integer isUpkeepValid(@RequestParam("deviceId") String deviceId, @RequestParam("period") Period period);

}
