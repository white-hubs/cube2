package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyStandardItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyStandardItemRelationMapper;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyStandardItemRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 执行标准和项关联关系
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStandardItemRelationServiceImpl extends ServiceImpl<DailyStandardItemRelationMapper, DailyStandardItemResultRelationPO> implements DailyStandardItemRelationService {
}
