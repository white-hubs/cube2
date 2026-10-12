package com.guhecloud.cube.biz.metric.server.metric.result.service.electron;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronHandlerEfficiencyRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronHandlerRateRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronLocationStatisticsMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronTotalMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolTaskClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.LocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.ElectronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.vo.LocationStatisticsVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 巡更
 */
@Slf4j
@Service
public class ElectronMetricService {
    private final PatrolTaskClient patrolTaskClient;

    public ElectronMetricService(PatrolTaskClient patrolTaskClient) {
        this.patrolTaskClient = patrolTaskClient;
    }

    /**
     * 电子巡更异常率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "ELECTRON_ERROR_RATE", description = "电子巡更异常率指标")
    public ElectronErrorRateMetricDTO electronErrRate(ElectronMetricParam param) {
        TotalNumberRateDTO dto = patrolTaskClient.getElectronErrRate(convertQueryParam(param));
        return ElectronErrorRateMetricDTO.builder().electronTotal(dto.getTotal()).electronAbnormalNumber(dto.getNumber())
                .electronAbnormalRate(dto.getRate()).build();
    }

    private ElectronQueryParam convertQueryParam(ElectronMetricParam param) {
        ElectronQueryParam queryParam = new ElectronQueryParam();
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPointName(param.getPointName());
        queryParam.setAreaName(param.getAreaName());
        return queryParam;

    }

    /**
     * 电子巡更完成率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "ELECTRON_COMPLETE_RATE", description = "电子巡更完成率指标")
    public ElectronCompleteRateMetricDTO electronCompleteRate(ElectronMetricParam param) {
        TotalNumberRateDTO dto = patrolTaskClient.getElectronCompleteRate(convertQueryParam(param));

        return ElectronCompleteRateMetricDTO.builder().electronTotal(dto.getTotal()).electronCompleteNumber(dto.getNumber())
                .electronCompleteRate(dto.getRate()).build();
    }

    /**
     * 电子巡更总数指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "ELECTRON_TASK_TOTAL", description = "电子巡更总数指标")
    public ElectronTotalMetricDTO electronTaskTotal(MetricParam param) {
        TotalNumberRateDTO taskTotal = patrolTaskClient.getTaskTotal(param.getPeriod());

        return ElectronTotalMetricDTO.builder().electronTotal(Objects.nonNull(taskTotal) ? taskTotal.getTotal() : 0).build();
    }

    /**
     * 电子巡更巡更点统计指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ELECTRON_TASK_LOCATIONS_STATISTICS_METRIC", description = "电子巡更巡更点统计指标")
    public List<ElectronLocationStatisticsMetricDTO> getElectronTaskLocationsStatisticsMetric(ElectronMetricParam param) {
        LocationQueryParam queryParam = new LocationQueryParam();
        queryParam.setPeriod(param.getPeriod());
        queryParam.setResult(param.getResult());
        List<LocationStatisticsVO> locations = patrolTaskClient.getLocationsStatistics(queryParam);
        return locations.stream().map(location ->
                                              ObjectUtils.copying(location, ElectronLocationStatisticsMetricDTO.class)
                                     ).collect(Collectors.toList());
    }

    /**
     * 电子巡更处理率指标
     */
    @MetricTopic(topic = "ELECTRON_HANDLE_RATE_RANK_METRIC", description = "电子巡更处理率指标")
    public ElectronHandlerRateRankMetricDTO electronHandleRateRankMetric(ElectronMetricParam param) {
        TotalNumberRateDTO dto = patrolTaskClient.getElectronCompleteRate(convertQueryParam(param));
        HandlerRateMetricDTO electronCompleteRateMetric = patrolTaskClient.getElectronCompleteRateMetric(param.getPeriod());
        return ElectronHandlerRateRankMetricDTO.builder()
                .patrolTaskTotal(dto.getTotal())
                .patrolTaskHandlerNumber(dto.getNumber())
                .patrolTaskHandledRate(Objects.nonNull(dto.getRate()) ? dto.getRate().doubleValue() : 0)
                .topHandlerRateDept(electronCompleteRateMetric.getTopHandlerRateDept())
                .bottomHandlerRateDept(electronCompleteRateMetric.getBottomHandlerRateDept())
                .topHandlerRateUser(electronCompleteRateMetric.getTopHandlerRateUser())
                .bottomHandlerRateUser(electronCompleteRateMetric.getBottomHandlerRateUser())
                .build();
    }

    /**
     * 电子巡更处理效率指标
     */
    @MetricTopic(topic = "ELECTRON_HANDLE_EFFICIENCY_RANK_METRIC", description = "电子巡更处理效率指标")
    public ElectronHandlerEfficiencyRankMetricDTO electronHandleEfficiencyRankMetric(MetricParam param) {
        TaskHandlerTimeVO completeTotalTime = patrolTaskClient.getElectronCompleteTotalTime(param.getPeriod());
        HandlerEfficiencyMetricDTO electronCompleteEfficiencyMetric = patrolTaskClient.getElectronCompleteEfficiencyMetric(param.getPeriod());
        double avgHandlerTime = 0;
        if (Objects.nonNull(completeTotalTime) && 0 != completeTotalTime.getTaskHandlerNumber()) {
            avgHandlerTime = BigDecimal.valueOf(completeTotalTime.getTaskTotalTime())
                    .divide(BigDecimal.valueOf(completeTotalTime.getTaskHandlerNumber()), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return ElectronHandlerEfficiencyRankMetricDTO.builder()
                .patrolTaskHandlerNumber(completeTotalTime.getTaskHandlerNumber())
                .patrolTaskTotalTime(completeTotalTime.getTaskTotalTime())
                .avgHandlerTime(avgHandlerTime)
                .topHandlerRateUser(electronCompleteEfficiencyMetric.getTopHandlerEfficiencyUser())
                .bottomHandlerRateUser(electronCompleteEfficiencyMetric.getBottomHandlerEfficiencyUser())
                .build();
    }

    public PatrolTaskRateMetricDTO getAllMetric(ElectronMetricParam param) {

        TotalNumberRateDTO completeRate = patrolTaskClient.getElectronCompleteRate(convertQueryParam(param));
        TotalNumberRateDTO errRate = patrolTaskClient.getElectronErrRate(convertQueryParam(param));
        return PatrolTaskRateMetricDTO.builder()
                .patrolTotal(completeRate.getTotal())
                .patrolCompleteNumber(completeRate.getNumber())
                .patrolCompleteRate(completeRate.getRate())
                .patrolAbnormalNumber(errRate.getNumber())
                .patrolAbnormalRate(errRate.getRate())
                .build();
    }
}
