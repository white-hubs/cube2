package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.TaskService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectDepStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskDepStatistics;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 巡检任务统计
 *
 * @author wql
 * @describe 巡检任务统计
 * @date 2024/1/23 17:44
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskStatisticsController {

    private final TaskService taskService;

    /**
     * 巡检任务总数统计
     *
     * @param period 时间范围
     *
     * @return 统计结果
     */
    @GetMapping("/inspect/task/total/statistics")
    public InspectionTaskStatisticsVO inspectionTaskNumStatistics(Period period) {
        Assert.isParameterError(period == null, "统计时间范围不允许为空!");
        return taskService.getInspectionTaskNumStatistics(period);
    }

    /**
     * 部门巡检任务统计
     *
     * @param param 统计参数
     *
     * @return 统计结果
     */
    @GetMapping("/inspect/task/dep/statistics")
    public List<TaskDepStatistics> inspectionDepTaskNumStatistics(InspectDepStatisticsParam param) {
        return taskService.inspectionDepTaskNumStatistics(param);
    }

    /**
     * 巡检任务完成率
     *
     * @param period 时间范围
     *
     * @return 巡检任务完成率
     */
    @GetMapping("/inspect/task/finished/ratio")
    public Float inspectionTaskFinishedRatio(Period period) {
        Assert.isParameterError(period == null, "统计时间范围不允许为空!");
        return taskService.inspectionTaskFinishedRatio(period, InspectTaskStatusEnum.OVER);
    }

}
