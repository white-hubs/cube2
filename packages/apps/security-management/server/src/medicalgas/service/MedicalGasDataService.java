package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.entity.MedicalGasData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.param.GasStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface MedicalGasDataService extends IService<MedicalGasData> {
    List<MedicalGasDataVo> getTodayGasUsage(GasStatisticsParam param);

    List<MedicalGasDataVo> getTodayGasUsageTop(GasStatisticsParam param);

    Map<String, Object> getGasUsageTrend(GasStatisticsParam param);

    Pagination<MedicalGasDataPageVo> getGasUsagePage(GasStatisticsParam param);
}
