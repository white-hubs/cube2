package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param.BoilerStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerRecordPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo.BoilerResultVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface BoilerService {

    Map<String, Object> getAlarmBimDeviceStatistics(BoilerStatisticsParam param);

    Map<String, Object> getDataOverview(BoilerStatisticsParam param);

    BoilerResultVo getDataTemperature(BoilerStatisticsParam param);

    BoilerResultVo getDataWater(BoilerStatisticsParam param);

    BoilerResultVo getDataElectricity(BoilerStatisticsParam param);

    Pagination<BoilerRecordPageVo> getBoilerDataRecordPage(BoilerParam param);

}
