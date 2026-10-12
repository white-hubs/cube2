package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.param.DailyBaseStatisticsParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.task.result.GridAbnormalTaskCountResult;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日常任务
 */
@Mapper
public interface DailyTaskMapper extends BaseMapper<DailyTaskPO> {
    Page<DailyTaskPO> selectTaskPage(@Param("page") Page<?> page, @Param("param") DailyTaskPageParam param,
                                     @Param("dailyTypeList") List<String> dailyTypeList
                                    );

    List<Integer> getTaskTenantList();

    List<String> getTaskType();

    Long selectTaskNumGroupByStatus(@Param("param") DailyBaseStatisticsParam param, @Param("states")
                                    List<String> states
                                   );

    Integer countTargetTaskNum(@Param("targetId") Long targetId, @Param("dailyType") String dailyType, @Param("period") Period period);

    List<Long> selectSnapshotTaskIds(@Param("taskIds") List<Long> taskIds);

    List<DailyTaskPO> selectGridAreaOpenTasks(@Param("gridAreaId") Long gridAreaId,
                                              @Param("dailyType") String dailyType,
                                              @Param("now") LocalDateTime now,
                                              @Param("statusList") List<String> statusList);

    List<GridAbnormalTaskCountResult> selectGridAbnormalTaskCount(@Param("gridAreaIds") List<Long> gridAreaIds,
                                                                  @Param("dailyType") String dailyType);
}
