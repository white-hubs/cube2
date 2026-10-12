package com.guhecloud.cube.biz.daily.task.domain.adapter.client;

import com.guhecloud.cube.biz.daily.task.domain.adapter.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.biz.daily.task.domain.adapter.param.DevicePostByIdSetParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 对象client
 */
@FeignClient(name = "iotDeviceCenter")
public interface DailyTargetDeviceClient {
    /**
     * 批量查询设备基础信息
     *
     * @param param deviceIdSet 设备id
     *
     * @return 设备信息
     */
    @PostMapping("/device/details/post/list")
    List<DeviceBaseDetailsDTO> postDetailsListByDeviceId(@RequestBody DevicePostByIdSetParam param);
}
