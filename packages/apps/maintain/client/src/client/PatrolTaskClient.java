package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.LocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.ElectronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.vo.LocationStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 巡更任务client
 */
@FeignClient(ServiceRoute.TAG)
public interface PatrolTaskClient {

    /**
     * 巡更异常率
     *
     * @return 率
     */
    @GetMapping("/electron/statistics/errRate")
    TotalNumberRateDTO getElectronErrRate(@SpringQueryMap ElectronQueryParam param);

    /**
     * 巡更完成率
     *
     * @return 率
     */
    @GetMapping("/electron/statistics/completeRate")
    TotalNumberRateDTO getElectronCompleteRate(@SpringQueryMap ElectronQueryParam param);

    /**
     * 获取任务总数
     *
     * @param period 时间
     *
     * @return 总数
     */
    @GetMapping("/electron/statistics/taskTotal")
    TotalNumberRateDTO getTaskTotal(@RequestParam("period") Period period);

    /**
     * 巡更点数量统计
     */
    @PostMapping("/electron/task/locations/statistics")
    List<LocationStatisticsVO> getLocationsStatistics(@RequestBody LocationQueryParam param);

    /**
     * 巡更处理率指标
     */
    @GetMapping("/electron/statistics/complete/rate/metric")
    HandlerRateMetricDTO getElectronCompleteRateMetric(@RequestParam("period") Period period);

    /**
     * 巡更处理总时长和处理数
     */
    @GetMapping("/electron/statistics/complete/total/time")
    TaskHandlerTimeVO getElectronCompleteTotalTime(@RequestParam("period") Period period);

    /**
     * 巡更处理效率指标
     */
    @GetMapping("/electron/statistics/complete/efficiency/metric")
    HandlerEfficiencyMetricDTO getElectronCompleteEfficiencyMetric(@RequestParam("period") Period period);
}
