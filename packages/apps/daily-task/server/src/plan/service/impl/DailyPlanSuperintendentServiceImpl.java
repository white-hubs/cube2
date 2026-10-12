package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import com.guhecloud.cube.biz.daily.task.domain.DailySuperintendentService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanSuperintendentMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanSuperintendentService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计划负责人
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanSuperintendentServiceImpl extends ServiceImpl<DailyPlanSuperintendentMapper, DailyPlanSuperintendentPO> implements DailyPlanSuperintendentService {
    private final DailySuperintendentService dailySuperintendentService;

    /**
     * 批量获取计划负责人
     *
     * @param planIdCollection 计划id集合
     *
     * @return 计划id和负责人集合关系的map
     */
    @Override
    public Map<Long, List<DailySuperintendentDTO>> getPlanSuperintendentByIds(Collection<Long> planIdCollection) {
        if (CollectionUtils.isEmpty(planIdCollection)) {
            return Collections.emptyMap();
        }
        // 查询负责对象集合
        var poList = this.lambdaQuery().in(DailyPlanSuperintendentPO::getMasterId, planIdCollection).list();
        if (CollectionUtils.isEmpty(poList)) {
            return Collections.emptyMap();
        }
        // 处理
        var dtoList = dailySuperintendentService.handlerSuperintendent(poList);
        return dtoList.stream().collect(Collectors.groupingBy(DailySuperintendentDTO::getMasterId));
    }

}
