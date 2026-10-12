package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanTargetStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanTargetStandardRelationMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanTargetStandardRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计划对象执行标准关联关系
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanTargetStandardRelationServiceImpl extends ServiceImpl<DailyPlanTargetStandardRelationMapper, DailyPlanTargetStandardRelationPO> implements DailyPlanTargetStandardRelationService {
}
