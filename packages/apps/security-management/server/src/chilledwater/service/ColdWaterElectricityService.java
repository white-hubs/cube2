package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWaterElectricity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;

import java.util.Map;

public interface ColdWaterElectricityService extends IService<ColdWaterElectricity> {
    Map<String, Object> getColdWaterElectricity(StatisticsParam param);
}
