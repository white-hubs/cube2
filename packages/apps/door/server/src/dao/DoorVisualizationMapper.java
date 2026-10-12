package com.guhecloud.cube.biz.door.server.dao;

import com.guhecloud.cube.biz.door.server.param.StatisticalChartParam;
import com.guhecloud.cube.biz.door.server.vo.DateTimeSlotVO;
import com.guhecloud.cube.biz.door.server.vo.PieChartValueVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoorVisualizationMapper {
    List<StatisticalChartDataVO> getCurrentList(@Param("param") StatisticalChartParam param,
                                                @Param("date") DateTimeSlotVO date, @Param("dimension") String dimension
                                               );

    Integer getPreviousList(@Param("param") StatisticalChartParam param,
                            @Param("date") DateTimeSlotVO date
                           );

    List<PieChartValueVO> getDoorThroughTypeCurrent(@Param("param") StatisticalChartParam param, @Param("date") DateTimeSlotVO date);

    List<PieChartValueVO> getDoorAccessSummary(@Param("param") StatisticalChartParam param, @Param("date") DateTimeSlotVO date);

    Integer getDoorThroughTypePervious(@Param("param") StatisticalChartParam param, @Param("date") DateTimeSlotVO date);

    List<PieChartValueVO> getDoorAnomalousTopCurrent(@Param("param") StatisticalChartParam param, @Param("date") DateTimeSlotVO date);

    List<String> getoutBusinessId(@Param("ids") List<Long> ids);

    Long getOnlineDevice(@Param("ids") List<Long> ids);

    Long getOfflineDevice(@Param("ids") List<Long> ids);
}
