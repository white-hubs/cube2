package com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.threedimensional.param.DeviceStatisticsParam;

import java.util.Map;

public interface DeviceStatisticsService {

    Map<String, Object> getEquipmentAlarmRate(DeviceStatisticsParam param);

}
