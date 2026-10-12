package com.guhecloud.cube.biz.daily.task.plan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyJobRecordPO;
import com.guhecloud.cube.biz.daily.task.plan.dao.DailyJobRecordMapper;
import com.guhecloud.cube.biz.daily.task.plan.service.DailyJobRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计划执行记录
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyJobRecordServiceImpl extends ServiceImpl<DailyJobRecordMapper, DailyJobRecordPO> implements DailyJobRecordService {
}
