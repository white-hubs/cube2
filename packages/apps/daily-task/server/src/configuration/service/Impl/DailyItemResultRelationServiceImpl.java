package com.guhecloud.cube.biz.daily.task.configuration.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyItemResultRelationPO;
import com.guhecloud.cube.biz.daily.task.configuration.dao.DailyItemResultRelationMapper;
import com.guhecloud.cube.biz.daily.task.configuration.service.DailyItemResultRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 执行项关联结果
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyItemResultRelationServiceImpl extends ServiceImpl<DailyItemResultRelationMapper, DailyItemResultRelationPO> implements DailyItemResultRelationService {
}
