package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetRelationPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanTargetRelationMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetRelationService;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 计划的对象
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanTargetRelationServiceImpl extends ServiceImpl<DailyPlanTargetRelationMapper, DailyPlanTargetRelationPO> implements DailyPlanTargetRelationService {
    @Override
    public Map<Pair<Long, Long>, DailyPlanTargetRelationPO> getTargetRelationMap(Set<Long> planIdSet) {
        if (CollectionUtils.isEmpty(planIdSet)) {
            return new HashMap<>();
        }
        List<DailyPlanTargetRelationPO> planTargetRelationPOS = this.lambdaQuery()
                .select(DailyPlanTargetRelationPO::getPlanId, DailyPlanTargetRelationPO::getRuleId, DailyPlanTargetRelationPO::getTargetId, DailyPlanTargetRelationPO::getTagIds)
                .in(DailyPlanTargetRelationPO::getPlanId, planIdSet)
                .isNotNull(DailyPlanTargetRelationPO::getRuleId)
                .list();
        if (CollectionUtils.isEmpty(planTargetRelationPOS)) {
            return new HashMap<>();
        }
        return planTargetRelationPOS.stream()
                .collect(Collectors.toMap(t -> Pair.of(t.getPlanId(), t.getTargetId()), Function.identity()));
    }
}
