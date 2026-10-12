package com.guhecloud.cube.biz.daily.task.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 日常任务客户端
 */
@FeignClient(name = "dailyTask")
public interface DailyTaskClient {

    /**
     * 批量查询网格异常任务数
     *
     * @param gridAreaIds 网格区域id集合
     * @param dailyType   日常任务类型
     *
     * @return 网格区域id与异常任务数映射
     */
    @PostMapping("/daily/task/grid/abnormal-count")
    Map<Long, Long> getGridAbnormalTaskCount(@RequestBody List<Long> gridAreaIds,
                                             @RequestParam("dailyType") String dailyType);
}
