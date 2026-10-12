package com.guhecloud.cube.biz.daily.task.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DailyPlanSuperintendentService extends IService<DailyPlanSuperintendentPO> {
    Map<Long, List<DailySuperintendentDTO>> getPlanSuperintendentByIds(Collection<Long> planIdCollection);
}
