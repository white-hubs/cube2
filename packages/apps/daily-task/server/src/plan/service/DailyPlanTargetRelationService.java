package com.guhecloud.cube.biz.daily.task.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import com.guhecloud.platform.base.model.Pair;

import java.util.Map;
import java.util.Set;

public interface DailyPlanTargetRelationService extends IService<DailyPlanTargetRelationPO> {
    Map<Pair<Long, Long>, DailyPlanTargetRelationPO> getTargetRelationMap(Set<Long> planIdSet);
}
