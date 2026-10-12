package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.entity.MedicalGasData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.param.GasStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MedicalGasDataMapper extends BaseMapper<MedicalGasData> {
    List<MedicalGasDataVo> getTodayGasUsage(@Param("param") GasStatisticsParam param, @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime
                                           );

    List<MedicalGasDataVo> getGasUsageTop(@Param("param") GasStatisticsParam param);

    List<MedicalGasDataVo> getGasUsageTrend(@Param("param") GasStatisticsParam param);

    Page<MedicalGasDataPageVo> getGasUsagePage(Page page, @Param("param") GasStatisticsParam param);
}
