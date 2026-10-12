package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author pengjian
 * @date 2022/8/1 15:22
 * @description
 */
@FeignClient("maintain")
public interface OutsourceRepairCheckClient {
    /**
     * 获取设备的委外维修次数
     *
     * @param deviceInstanceId
     * @param period
     *
     * @return
     */
    @GetMapping("/outsource/repair/number")
    Integer getRepairNumberByDeviceId(
            @RequestParam("deviceInstanceId") Long deviceInstanceId,
            @RequestParam("period") Period period
                                     );

}
