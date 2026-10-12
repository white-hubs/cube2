package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.GearPosition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataKeyValueVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DevicePositionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GearPositionMapper extends BaseMapper<GearPosition> {
    Page<DevicePositionVo> getDevicePosition(Page page, @Param("keyword") String keyword);

    BigDecimal getStatisticsData(@Param("dataColumn") String dataColumn, @Param("tableName") String tableName,
                                 @Param("startTime") String startTime, @Param("endTime") String endTime,
                                 @Param("deviceCode") String deviceCode, @Param("param") StatisticsParam param
                                );

    List<DataKeyValueVo> getStatisticsDataTrend(@Param("dataColumn") String dataColumn, @Param("tableName") String tableName,
                                                @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                @Param("param") StatisticsParam param
                                               );
}
