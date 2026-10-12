package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanExpandPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanExpandMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyPlanExpandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计划附加信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyPlanExpandServiceImpl extends ServiceImpl<DailyPlanExpandMapper, DailyPlanExpandPO> implements DailyPlanExpandService {
}
