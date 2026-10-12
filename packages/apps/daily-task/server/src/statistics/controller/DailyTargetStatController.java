package com.guhecloud.cube.biz.daily.task.statistics.controller;

import com.guhecloud.cube.biz.daily.task.commons.vo.statistics.TargetDailyStatVO;
import com.guhecloud.cube.biz.daily.task.statistics.service.DailyTargetStatService;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTargetStatController {

    private final DailyTargetStatService dailyTargetStatService;

    /**
     * 获取目标日常统计
     *
     * @param targetId  目标ID
     * @param dailyType 日常类型
     * @param period    时间范围
     *
     * @return 目标日常统计
     */
    @GetMapping("/target/{targetId}/statistics")
    public TargetDailyStatVO getTargetDailyStat(@PathVariable("targetId") Long targetId,
                                                @RequestParam String dailyType,
                                                @RequestParam(required = false) Period period
                                               ) {
        TargetDailyStatVO vo = new TargetDailyStatVO();
        vo.setPlanNum(dailyTargetStatService.getTargetPlanNum(targetId, dailyType, period));
        vo.setTaskNum(dailyTargetStatService.getTargetTaskNum(targetId, dailyType, period));
        vo.setAbnormalNum(dailyTargetStatService.getTargetAbnormalNum(targetId, dailyType, period));
        return vo;
    }
}
