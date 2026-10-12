package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskExpandPO;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskExpandMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskExpandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 任务扩展信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskExpandServiceImpl extends ServiceImpl<DailyTaskExpandMapper, DailyTaskExpandPO> implements DailyTaskExpandService {
}
