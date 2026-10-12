package com.guhecloud.cube.biz.daily.task.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetRefResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetPO;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 任务对象
 */
@Mapper
public interface DailyTaskTargetMapper extends BaseMapper<DailyTaskTargetPO> {
    Page<DailyTaskTargetResult> selectDailyTaskTargetPage(@Param("page") Page<?> page, @Param("param") DailyTaskTargetQueryParam param);

    List<DailyTaskTargetRefResult> selectCurrentUserWaitHandlerTask(@Param("userId") Long userId, @Param("targetIds") Set<Long> targetIds);
}
