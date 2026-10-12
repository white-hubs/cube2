package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaStatQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionHourlyPersonFlowPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonFlowRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TrafficDetectionHourlyPersonFlowMapper extends BaseMapper<TrafficDetectionHourlyPersonFlowPO> {

    Page<TrafficDetectionAreaPersonFlowRankVO> selectAreaPersonFlowRankPage(Page<?> page,
                                                                             @Param("param") TrafficDetectionAreaStatQueryParam param,
                                                                             @Param("begin") LocalDateTime begin,
                                                                             @Param("end") LocalDateTime end,
                                                                             @Param("granularity") String granularity,
                                                                             @Param("bucketCount") Integer bucketCount,
                                                                             @Param("defaultPeakPeriod") String defaultPeakPeriod);

    List<TrafficDetectionAreaPersonFlowRankVO> selectAreaPersonFlowRankList(@Param("param") TrafficDetectionAreaStatQueryParam param,
                                                                             @Param("begin") LocalDateTime begin,
                                                                             @Param("end") LocalDateTime end,
                                                                             @Param("granularity") String granularity,
                                                                             @Param("bucketCount") Integer bucketCount,
                                                                             @Param("defaultPeakPeriod") String defaultPeakPeriod);
}
