package com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.param.PerimeterStatisticsParam;

import java.util.Map;

public interface PerimeterService {
    Map<String, Object> getAreaOfflineNum(PerimeterStatisticsParam param);
}
