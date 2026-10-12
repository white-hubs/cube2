package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.LooseWorkNumberMetricParam;
import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleMetricVO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PROJECT_SCHEDULE_METRIC_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/16 13:51
 * @Description:
 */
@Slf4j
@Service
public class HeadBaseLooseWorksMetricService {
    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadBaseLooseWorksMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    private List<MarketMetricResult<ProjectScheduleMetricVO>> getMetricType(MetricParam param, MetricType metricType) {
        LooseWorkNumberMetricParam queryParam = new LooseWorkNumberMetricParam();
        queryParam.setPeriod(param.getPeriod());
        queryParam.setMetricType(metricType);
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PROJECT_SCHEDULE_METRIC_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<ProjectScheduleMetricVO>() {
                });
        return allMarketHiddenMetrics;
    }

    @MetricTopic(topic = "HEAD_BASE_PROJECT_NUMBER", description = "总部小散工程数量")
    public Long projectNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
        return allMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 总部小散工程完成数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_COMPLETED_Number", description = "总部小散工程完成数")
    public Long projectCompletedNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> completedMarketHiddenMetrics = getMetricType(param, MetricType.COMPLETED_RATE);

        return completedMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 总部小散工程异常数
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_ABNORMAL_NUMBER", description = "总部小散工程异常数")
    public Long projectAbnormalNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> abnormalMarketHiddenMetrics = getMetricType(param, MetricType.ABNORMAL_RATE);
        return abnormalMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 总部小散工程整改数
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_REFORM_NUMBER", description = "总部小散工程整改数")
    public Long projectReformNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_RATE);
        return reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 总部小散工程验收数
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_ACCEPTANCE_NUMBER", description = "总部小散工程验收数")
    public Long projectAcceptanceNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.ACCEPTANCE_RATE);
        return reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 小散工程处理率前五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_PROCESS_RATE_TOP_FIVE", description = "小散工程处理率前五的市场")
    public List<HandlerRateDTO> projectProcessRateTopFive(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateDTOS(param, MetricType.COMPLETED_RATE);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getHandlerRateDTOS(MetricParam param, MetricType metricType) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> completedMarketHiddenMetrics = getMetricType(param, metricType);
        List<HandlerRateDTO> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<ProjectScheduleMetricVO> marketMetricResult : completedMarketHiddenMetrics) {
            if (Objects.isNull(marketMetricResult.getMetricStruct())) {
                continue;
            }
            marketCompletedRateMetrics.add(HandlerRateDTO.builder()
                                                   .marketName(marketMetricResult.getMarketName())
                                                   .num(marketMetricResult.getMetricStruct().getNumber())
                                                   .rate(Objects.nonNull(marketMetricResult.getMetricStruct().getRate())
                                                         ? marketMetricResult.getMetricStruct().getRate()
                                                         : 0)
                                                   .build());
        }
        return marketCompletedRateMetrics;
    }

    /**
     * 小散工程处理率后五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_PROCESS_RATE_BOTTOM_FIVE", description = "小散工程处理率后五的市场")
    public List<HandlerRateDTO> projectProcessRateBottomFive(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateDTOS(param, MetricType.COMPLETED_RATE);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 小散工程验收率前五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_ACCEPTANCE_RATE_TOP_FIVE", description = "小散工程验收率前五的市场")
    public List<HandlerRateDTO> projectAcceptanceRateTopFive(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateDTOS(param, MetricType.ACCEPTANCE_RATE);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 小散工程验收率后五的市场
     */
    @MetricTopic(topic = "HEAD_BASE_PROJECT_ACCEPTANCE_RATE_BOTTOM_FIVE", description = "小散工程验收率后五的市场")
    public List<HandlerRateDTO> projectAcceptanceRateBottomFive(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateDTOS(param, MetricType.ACCEPTANCE_RATE);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

}
