package com.guhecloud.cube.srv.gen.securitymanagement.server.logistics.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.param.PerimeterStatisticsParam;

import java.util.Map;

public interface LogisticsService {
    Map<String, Object> getLogisticsAreaStatusNum(PerimeterStatisticsParam param);
}
