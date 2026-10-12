package com.guhecloud.cube.biz.work.center.server.controller;

import com.guhecloud.cube.biz.work.center.server.service.WorkTaskStatisticsService;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkBusinessTypeVO;
import com.guhecloud.cube.biz.work.center.server.vo.WorkTaskTrendStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作统计管理接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkTaskStatisticsController {

    private final WorkTaskStatisticsService workTaskStatisticsService;

    /**
     * 工作数量统计
     *
     * @return
     */
    @GetMapping("/work-task-statistics")
    public Long workNumTotal() {
        return this.workTaskStatisticsService.workNumTotal();
    }

    /**
     * 工作业务类型统计
     *
     * @return
     */
    @GetMapping("/work-task-statistics/business-type-statistics")
    public List<WorkBusinessTypeStatisticsVO> workBusinessTypeWorkStatistics() {
        return this.workTaskStatisticsService.workBusinessTypeWorkStatistics();
    }

    /**
     * 工作业务类型
     *
     * @return
     */
    @GetMapping("/work-task-statistics/business-type")
    public List<WorkBusinessTypeVO> workBusinessTypes() {
        return this.workTaskStatisticsService.workBusinessTypes();
    }

    /**
     * 工作业务类型
     *
     * @return
     */
    @GetMapping("/work-task-statistics/done/business-type")
    public List<WorkBusinessTypeVO> workDoneBusinessTypes() {
        return this.workTaskStatisticsService.workDoneBusinessTypes();
    }

    /**
     * 近30天工作趋势统计
     *
     * @return
     */
    @GetMapping("/work-task-statistics/last-thirty-days-trend")
    public List<WorkTaskTrendStatisticsVO> workTaskLastThirtyDaysTrendStatistics() {
        return this.workTaskStatisticsService.workTaskLastThirtyDaysTrendStatistics();
    }
}
