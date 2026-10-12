package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao;

import com.guhecloud.cube.biz.subsystem.parking.client.vo.DateTimeSlotVO;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkStatisticsVo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ParkingVisualizationMapper {
    ParkingOverviewVO getParkingOverview(@Param("source") String source);

    MoneyOverviewVO getMoneyOverviewCurrent(@Param("date") DateTimeSlotVO date);

    List<StatisticalChartDataVO> getTrafficTrend(@Param("date") DateTimeSlotVO date,
                                                 @Param("accessType") String accessType, @Param("dimension") String dimension
                                                );

    List<String> getRecordNumber(@Param("number") Integer number, @Param("date") DateTimeSlotVO date);

    /**
     * 滞留车辆按天数分桶统计（有进无出，滞留完整天数 >= 1，仅近 lookback 窗口内进场）。
     * name 为桶下标 0~4，valus 为数量。
     */
    List<StatisticalChartDataVO> getRetentionTrafficTrendBuckets(@Param("now") LocalDateTime now,
                                                                 @Param("lookbackBegin") LocalDateTime lookbackBegin,
                                                                 @Param("strandedBefore") LocalDateTime strandedBefore);

    /**
     * 停车时长分布分桶。name=桶下标 0~3，valus=数量。
     * 统计时间窗内恰好一进一出的 identify_id，按时长（天，对齐旧 ChronoUnit.DAYS）分桶。
     */
    List<StatisticalChartDataVO> getParkingDurationBuckets(@Param("date") DateTimeSlotVO date);

    List<MoneyTypeVO> getPayType(@Param("date") DateTimeSlotVO date);

    List<StatisticalChartDataVO> getPayTrend(@Param("dimension") String dimension, @Param("date") DateTimeSlotVO date);

    ParkStatisticsVo getParkEventInOrOut(@Param("param") ParkingStatisticsQueryParam param);
}
