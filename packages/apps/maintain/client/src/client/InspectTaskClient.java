package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionObjStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionObjQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 巡检任务client
 */
@FeignClient(ServiceRoute.TAG)
public interface InspectTaskClient {
    /**
     * 获取任务总数
     *
     * @param param 时间
     *
     * @return 总数
     */
    @GetMapping("/inspect-task/taskTotal")
    Long getTaskTotal(@SpringQueryMap InspectionCommonParam param);

    /**
     * 获取任务总数
     *
     * @param param 时间
     *
     * @return 总数
     */
    @PostMapping("/inspect-task/taskTotal/post")
    Long postTaskTotal(@RequestBody InspectionCommonParam param);

    /**
     * 巡捡处理率指标
     */
    @GetMapping("/inspect-task/complete/rate/metric")
    HandlerRateMetricDTO getInspectTaskCompleteRateMetric(@RequestParam("period") Period period);

    /**
     * 巡检处理效率指标
     */
    @GetMapping("/inspect-task/complete/efficiency/metric")
    HandlerEfficiencyMetricDTO getInspectTaskCompleteEfficiencyMetric(@RequestParam("period") Period period);

    /**
     * 巡检处理数、处理总时长
     */
    @GetMapping("/inspect-task/complete/time/num/metric")
    TaskHandlerTimeVO getInspectTaskTimeCompleteTimeNumMetric(@RequestParam("period") Period period);

    /**
     * 根据巡检对象进行查询
     *
     * @param queryParam
     *
     * @return
     */
    @GetMapping("/statistic-analysis/objStatistics")
    Pagination<InspectionObjStatisticsVO> objStatistics(@SpringQueryMap InspectionObjQueryParam queryParam);
}
