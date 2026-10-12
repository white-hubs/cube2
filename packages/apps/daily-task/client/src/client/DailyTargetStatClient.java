package com.guhecloud.cube.biz.daily.task.client;

import com.guhecloud.cube.biz.daily.task.commons.vo.statistics.TargetDailyStatVO;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 日常目标统计客户端
 */
@FeignClient(name = "dailyTask")
public interface DailyTargetStatClient {

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
    TargetDailyStatVO getTargetDailyStat(@PathVariable("targetId") Long targetId,
                                         @RequestParam String dailyType,
                                         @RequestParam(required = false) Period period
                                        );
}
