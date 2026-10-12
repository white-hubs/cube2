package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.cube.iot.mgt.north.param.DeviceEventQueryParam;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthDeviceEventService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/north/device/event")
public class NorthDeviceEventController {
    @Resource
    private NorthDeviceEventService northDeviceEventService;

    /**
     * 单设备历史事件
     *
     * @param deviceEventQueryParam deviceEventQueryParam
     *
     * @return page
     */
    @GetMapping("/history/page")
    public Pagination<DeviceEventHistoryResultVo> page(@SpringQueryMap DeviceEventQueryParam deviceEventQueryParam) {
        return northDeviceEventService.page(SessionContextHolder.getRequester().getTenant(), deviceEventQueryParam);
    }
}
