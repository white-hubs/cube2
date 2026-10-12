package com.guhecloud.cube.biz.metric.server.metric.result.service.electron;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.client.PatrolTaskClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.ElectronQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 19:18
 * @Description:
 */
@Slf4j
@Service
public class BaseElectronMetricService {
    private final PatrolTaskClient patrolTaskClient;

    public BaseElectronMetricService(PatrolTaskClient patrolTaskClient) {this.patrolTaskClient = patrolTaskClient;}

    /**
     * 电子巡更数
     */
    @MetricTopic(topic = "BASE_ELECTRON__NUM_METRIC", description = "电子巡更数")
    public Long electronNumMetric(MetricParam param) {
        ElectronQueryParam queryParam = new ElectronQueryParam();
        queryParam.setPeriod(param.getPeriod());
        TotalNumberRateDTO dto = patrolTaskClient.getElectronCompleteRate(queryParam);
        return dto.getTotal();
    }

    /**
     * 电子巡更处理数
     */
    @MetricTopic(topic = "BASE_ELECTRON_COMPLETE_NUM_METRIC", description = "电子巡更处理数")
    public Long electronCompleteNumMetric(MetricParam param) {
        ElectronQueryParam queryParam = new ElectronQueryParam();
        queryParam.setPeriod(param.getPeriod());
        TotalNumberRateDTO dto = patrolTaskClient.getElectronCompleteRate(queryParam);
        return dto.getNumber();
    }

    /**
     * 电子巡更完成率
     */
    @MetricTopic(topic = "BASE_ELECTRON_COMPLETE_RATE_METRIC", description = "电子巡更完成率")
    public Double electronCompleteRateMetric(MetricParam param) {
        ElectronQueryParam queryParam = new ElectronQueryParam();
        queryParam.setPeriod(param.getPeriod());
        TotalNumberRateDTO dto = patrolTaskClient.getElectronCompleteRate(queryParam);
        return Objects.nonNull(dto.getRate()) ? dto.getRate().doubleValue() : 0;
    }

    /**
     * 电子巡更处理率最高的五个部门
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_RATE_TOP_METRIC", description = "电子巡更处理率最高的五个部门")
    public List<HandlerRateDTO> electronHandleRateTopMetric(MetricParam param) {
        HandlerRateMetricDTO electronCompleteRateMetric = patrolTaskClient.getElectronCompleteRateMetric(param.getPeriod());
        return electronCompleteRateMetric.getTopHandlerRateDept();
    }

    /**
     * 电子巡更处理率最低的五个部门
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_RATE_BOTTOM_METRIC", description = "电子巡更处理率最低的五个部门")
    public List<HandlerRateDTO> electronHandleRateBottomMetric(MetricParam param) {
        HandlerRateMetricDTO electronCompleteRateMetric = patrolTaskClient.getElectronCompleteRateMetric(param.getPeriod());
        return electronCompleteRateMetric.getBottomHandlerRateDept();
    }

    /**
     * 电子巡更处理率最高的五个人
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_RATE_TOP_USER_METRIC", description = "电子巡更处理率最高的五个人")
    public List<HandlerRateDTO> electronHandleRateTopUserMetric(MetricParam param) {
        HandlerRateMetricDTO electronCompleteRateMetric = patrolTaskClient.getElectronCompleteRateMetric(param.getPeriod());
        return electronCompleteRateMetric.getTopHandlerRateUser();
    }

    /**
     * 电子巡更处理率最低的五个人
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_RATE_BOTTOM_USER_METRIC", description = "电子巡更处理率最低的五个人")
    public List<HandlerRateDTO> electronHandleRateBottomUserMetric(MetricParam param) {
        HandlerRateMetricDTO electronCompleteRateMetric = patrolTaskClient.getElectronCompleteRateMetric(param.getPeriod());
        return electronCompleteRateMetric.getBottomHandlerRateUser();
    }

    /**
     * 电子巡更总处理时长
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_TIME_TOTAL_METRIC", description = "电子巡更总处理时长")
    public Long electronHandleTimeTotalMetric(MetricParam param) {
        TaskHandlerTimeVO completeTotalTime = patrolTaskClient.getElectronCompleteTotalTime(param.getPeriod());
        return Objects.nonNull(completeTotalTime) ? completeTotalTime.getTaskTotalTime() : 0;
    }

    /**
     * 电子巡更处理效率最高的五个人
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_EFFICIENCY_RANK_METRIC", description = "电子巡更处理效率指标")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyRankMetric(MetricParam param) {
        HandlerEfficiencyMetricDTO electronCompleteEfficiencyMetric = patrolTaskClient.getElectronCompleteEfficiencyMetric(param.getPeriod());
        return electronCompleteEfficiencyMetric.getTopHandlerEfficiencyUser();
    }

    /**
     * 电子巡更处理效率最低的五个人
     */
    @MetricTopic(topic = "BASE_ELECTRON_HANDLE_EFFICIENCY_BOTTOM_METRIC", description = "电子巡更处理效率最低的五个人")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyBottomMetric(MetricParam param) {
        HandlerEfficiencyMetricDTO electronCompleteEfficiencyMetric = patrolTaskClient.getElectronCompleteEfficiencyMetric(param.getPeriod());
        return electronCompleteEfficiencyMetric.getBottomHandlerEfficiencyUser();
    }
}
