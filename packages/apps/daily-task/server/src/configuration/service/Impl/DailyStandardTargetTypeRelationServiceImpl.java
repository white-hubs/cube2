package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardTargetTypeRelationPO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyStandardTargetTypeRelationMapper;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardTargetTypeRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 执行标准与目标类型关联关系
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStandardTargetTypeRelationServiceImpl extends ServiceImpl<DailyStandardTargetTypeRelationMapper, DailyStandardTargetTypeRelationPO> implements DailyStandardTargetTypeRelationService {
}

