package com.guhecloud.cube.biz.daily.task.statistics.service.impl;

import com.guhecloud.cube.biz.daily.task.plan.dao.DailyPlanMapper;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordMapper;
import com.guhecloud.cube.biz.daily.task.statistics.service.DailyTargetStatService;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskMapper;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTargetStatServiceImpl implements DailyTargetStatService {

    private final DailyPlanMapper dailyPlanMapper;

    private final DailyTaskMapper dailyTaskMapper;

    private final DailyRecordMapper dailyRecordMapper;

    @Override
    public Integer getTargetPlanNum(Long targetId, String dailyType, Period period) {
        return this.dailyPlanMapper.countTargetPlanNum(targetId, dailyType, period);
    }

    @Override
    public Integer getTargetTaskNum(Long targetId, String dailyType, Period period) {
        return this.dailyTaskMapper.countTargetTaskNum(targetId, dailyType, period);
    }

    @Override
    public Integer getTargetAbnormalNum(Long targetId, String dailyType, Period period) {
        return this.dailyRecordMapper.countTargetAbnormalNum(targetId, dailyType, period);
    }
}
