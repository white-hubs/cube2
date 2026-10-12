package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceEventHistoryDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceEventQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotDeviceEventClient {

    /**
     * 单设备历史事件
     *
     * @param deviceEventQueryParam deviceEventQueryParam
     *
     * @return page
     */
    @GetMapping("/north/device/event/history/page")
    Pagination<DeviceEventHistoryDTO> page(@SpringQueryMap DeviceEventQueryParam deviceEventQueryParam);
}
