package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanStandardRelationPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanStandardRelationMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanStandardRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计划执行标准关联
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanStandardRelationServiceImpl extends ServiceImpl<DailyPlanStandardRelationMapper, DailyPlanStandardRelationPO> implements DailyPlanStandardRelationService {
}
