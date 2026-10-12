package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AlarmDeviceStatusParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface ColdWaterAlarmService {
    Pagination<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalPage(AlarmDeviceStatusParam param);

    List<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalExcel(AlarmDeviceStatusParam param);
}
