package com.guhecloud.cube.biz.daily.task.plan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.plan.dto.DailyPlanGenerateTaskAfterUpdateDTO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计划
 */
@Mapper
public interface DailyPlanMapper extends BaseMapper<DailyPlanPO> {
    void batchUpdateMultiFields(@Param("updates") List<DailyPlanGenerateTaskAfterUpdateDTO> updates);

    Integer countTargetPlanNum(@Param("targetId") Long targetId, @Param("dailyType") String dailyType, @Param("period") Period period);
}
