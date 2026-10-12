package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskCompletionRateStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskStatusStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaintainPatrolStatisticsMapper {
    Long getPatrolTaskNumber(@Param("param") BaseDateStatisticsParam param);

    TaskCompletionRateStatisticsVO getPatrolTaskCompletionRate(@Param("param") BaseDateStatisticsParam param);

    TaskStatusStatisticsVO getTaskStatusStatistics(@Param("param") BaseDateStatisticsParam param);

    Long getPatrolTaskDistinctRouteNumber(@Param("param") BaseDateStatisticsParam param);

    Long getPatrolTaskDistinctLocationNumber(@Param("param") BaseDateStatisticsParam param);
}
