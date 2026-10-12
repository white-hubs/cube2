package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity.EnvironmentalMonitoring;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.DateParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalMonitoringVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.vo.EnvironmentalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EnvironmentalMonitoringMapper extends BaseMapper<EnvironmentalMonitoring> {

    List<EnvironmentalVo> getMaxData(@Param("dataField") String dataField, @Param("timeField") String timeField,
                                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
                                    );

    List<EnvironmentalVo> getMinData(@Param("dataField") String dataField, @Param("timeField") String timeField,
                                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
                                    );

    List<EnvironmentalVo> getTrendAnalysisCoConcentrationTunnels(@Param("startTime") LocalDateTime startTime,
                                                                 @Param("endTime") LocalDateTime endTime,
                                                                 @Param("paragraph") String paragraph,
                                                                 @Param("dateFormat") String dateFormat
                                                                );

    List<EnvironmentalVo> trendAnalysisRealTime(@Param("dataField") String dataField, @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime, @Param("paragraph") String paragraph,
                                                @Param("dateFormat") String dateFormat
                                               );

    Page<EnvironmentalMonitoringVo> getEnvironmentalPage(Page page, @Param("param") DateParam param, @Param("dataField") String dataField);

}
