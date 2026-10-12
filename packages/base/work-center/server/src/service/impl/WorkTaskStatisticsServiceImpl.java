package com.guhecloud.cube.biz.work.center.server.service.impl;

import com.guhecloud.cube.biz.work.center.server.dao.WorkTaskMapper;
import com.guhecloud.cube.biz.work.center.server.service.WorkTaskStatisticsService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskStatisticsServiceImpl implements WorkTaskStatisticsService {

    private final WorkTaskMapper workTaskMapper;

    @Override
    public Long allWorkNumTotal(Period period) {
        return this.workTaskMapper.selectAllWorkNumTotal(period);
    }

    @Override
    public Long workNumTotal() {
        return this.workTaskMapper.selectWorkNumTotal(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<WorkBusinessTypeStatisticsVO> allWorkBusinessTypeWorkStatistics(Period period) {
        return this.workTaskMapper.selectAllWorkBusinessTypeWorkStatistics(period);
    }

    @Override
    public List<WorkBusinessTypeStatisticsVO> workBusinessTypeWorkStatistics() {
        return this.workTaskMapper.selectWorkBusinessTypeWorkStatistics(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<WorkBusinessTypeVO> workBusinessTypes() {
        return this.workTaskMapper.selectWorkBusinessTypes();
    }

    @Override
    public List<WorkBusinessTypeVO> workDoneBusinessTypes() {
        return this.workTaskMapper.selectWorkDoneBusinessTypes(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<WorkTaskTrendStatisticsVO> workTaskLastThirtyDaysTrendStatistics() {
        LocalDateTime endDateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startDateTime = DateUtils.beginOfDay(DateUtils.offsetDay(endDateTime, -30));
        Period period = new Period(startDateTime, endDateTime);
        List<WorkTaskTrendStatisticsVO> workTaskTrendStatisticsVOS = this.workTaskMapper.selectWorkTaskTrendStatistics(SecurityContextHolder
                                                                                                                               .getPrincipal()
                                                                                                                               .getId(), period);
        List<WorkTaskTrendStatisticsVO> finalDatas = new ArrayList<>();
        for (LocalDateTime start = startDateTime;
             start.isBefore(endDateTime) || start.isEqual(endDateTime); start = DateUtils.offsetDay(start, 1)) {
            String date = DateUtils.formatDate(start);
            WorkTaskTrendStatisticsVO trendStatVO = workTaskTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date)).findAny()
                    .orElse(null);
            finalDatas.add(trendStatVO == null ? new WorkTaskTrendStatisticsVO(0L, date) : trendStatVO);
        }
        return finalDatas;
    }
}
