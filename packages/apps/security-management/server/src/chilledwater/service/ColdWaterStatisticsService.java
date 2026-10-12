package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DeviceHistoryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface ColdWaterStatisticsService {
    //基础数据
    Map<String, Object> basicDataReportAnalysis();

    //商户活跃数据
    Map<String, Object> merchantActivityData(StatisticsParam statisticsParam);

    Map<String, Object> getChilledWaterData(StatisticsParam statisticsParam);

    //设备历史
    Map<String, Object> getDeviceHistory(DeviceHistoryParam param);

    //历史属性分页
    Pagination<KeyValueWithTsDTO> getDeviceAttributeDataHistory(DeviceHistoryParam param);
}
