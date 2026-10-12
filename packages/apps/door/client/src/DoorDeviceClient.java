package com.guhecloud.cube.biz.door.client;

import com.guhecloud.cube.biz.door.client.param.DoorDeviceQueryParam;
import com.guhecloud.cube.biz.door.client.vo.DoorDeviceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 18:25
 * @Description:
 */

@FeignClient(name = "door")
public interface DoorDeviceClient {
    /**
     * @Description 门禁设备查询
     * @Author lrj
     */
    @GetMapping("/get/door/device/list")
    List<DoorDeviceVo> getDoorDeviceList(@SpringQueryMap DoorDeviceQueryParam doorDeviceQueryParam);
}
