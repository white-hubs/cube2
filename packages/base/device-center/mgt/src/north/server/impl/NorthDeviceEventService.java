package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.north.param.DeviceEventQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NorthDeviceEventService {
    @Resource
    private TimeSeriesInterface timeSeriesInterface;

    public Pagination<DeviceEventHistoryResultVo> page(Integer tenant, DeviceEventQueryParam deviceEventQueryParam) {
        IPage<DeviceEventHistoryResultVo> result = timeSeriesInterface.querySingleDeviceEventPage(
                toEventHistoryQueryVo(tenant, deviceEventQueryParam),
                deviceEventQueryParam.getPageNumber(),
                deviceEventQueryParam.getPageSize());

        return Pagination.<DeviceEventHistoryResultVo>builder()
                .total(result.getTotal())
                .pages(result.getPages())
                .records(result.getRecords())
                .build();
    }

    private DeviceEventHistoryQueryVo toEventHistoryQueryVo(Integer tenant, DeviceEventQueryParam deviceEventQueryParam) {
        DeviceEventHistoryQueryVo deviceEventHistoryQueryVo = new DeviceEventHistoryQueryVo();
        deviceEventHistoryQueryVo.setTenantId(String.valueOf(tenant));
        deviceEventHistoryQueryVo.setDeviceId(deviceEventQueryParam.getDeviceId());
        deviceEventHistoryQueryVo.setEventCodes(deviceEventQueryParam.getEventCodes());
        deviceEventHistoryQueryVo.setBizType(deviceEventQueryParam.getBizType());

        if (Objects.nonNull(deviceEventQueryParam.getStartTs()) && deviceEventQueryParam.getStartTs() >= 0 &&
            Objects.nonNull(deviceEventQueryParam.getEndTs()) && deviceEventQueryParam.getEndTs() > 0) {
            deviceEventHistoryQueryVo.setStartTime(DateUtils.format(DateUtils.parse(deviceEventQueryParam.getStartTs())));
            deviceEventHistoryQueryVo.setEndTime(DateUtils.format(DateUtils.parse(deviceEventQueryParam.getEndTs())));
        }

        if (StringUtils.isNotBlank(deviceEventQueryParam.getOrder())) {
            deviceEventHistoryQueryVo.setOrder(deviceEventQueryParam.getOrder());
        }
        if (StringUtils.isBlank(deviceEventHistoryQueryVo.getBizType())) {
            deviceEventHistoryQueryVo.setBizType("touch");
        }
        return deviceEventHistoryQueryVo;
    }
}
