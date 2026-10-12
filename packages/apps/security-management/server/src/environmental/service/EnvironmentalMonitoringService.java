package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity.EnvironmentalMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.DateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface EnvironmentalMonitoringService extends IService<EnvironmentalMonitoring> {
    Map<String, Object> getEnvironmentalIndicators();

    Map<String, Object> getCotRendAnalysis();

    Map<String, Object> trendAnalysisRealTime(String paragraph);

    Pagination<EnvironmentalMonitoringVo> getEnvironmentalPage(DateParam param);
}
