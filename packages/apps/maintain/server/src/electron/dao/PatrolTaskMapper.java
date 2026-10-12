package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationRouteStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 巡更任务
 * @date 2023/5/29 11:28
 */
@Mapper
public interface PatrolTaskMapper extends BaseMapper<PatrolTask> {
    List<LocationRouteStatisticsVO> locationRouteStatistics(@Param("period") Period period);

    /**
     * 巡检记录项总部数据采集
     *
     * @param tenant 租户
     * @param period 时间段
     *
     * @return 集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<PatrolTask> getPatrolTaskCollectData(@Param("tenant") Integer tenant, @Param("period") Period period);

    /**
     * 巡更点有异常的巡检任务
     * 只要有个巡更点就算一条
     *
     * @param period 时间段
     * @param takIds
     *
     * @return
     */
    List<PatrolTask> getPatrolTaskErrorDataCount(@Param("period") Period period, @Param("takIds") Set<Long> takIds);

    List<HandlerRateDTO> getTop5FinishRateUser(@Param("period") Period period, @Param("sort") String sort);

    List<HandlerRateDTO> getTop5FinishRateDept(@Param("period") Period period, @Param("sort") String sort);

    List<HandlerEfficiencyDTO> getTop5FinishEfficiencyDept(@Param("period") Period period, @Param("sort") String sort);

    List<HandlerEfficiencyDTO> getTop5FinishEfficiencyUser(@Param("period") Period period, @Param("sort") String sort);

    TaskHandlerTimeVO selectElectronCompleteTotalTime(@Param("period") Period period);
}
