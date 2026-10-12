package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.HeadLoosWorksReformEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.HeadLoosWorksReformRateDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.LooseWorkNumberMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.HeadquartersAcceptanceRateMetric;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkAbnormalRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkReformEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.LooseWorkReformRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.looseworks.dto.ProjectScheduleAllMetricDTO;
import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleMetricVO;
import com.guhecloud.platform.base.model.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PROJECT_SCHEDULE_METRIC_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/16 13:52
 * @Description:
 */
@Slf4j
@Service
public class HeadquartersLooseWorksMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadquartersLooseWorksMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    @MetricTopic(topic = "HEADQUARTERS_PROJECT_NUMBER", description = "小散工程数量指标")
    public LooseWorkNumberMetricDTO projectCompletedNumber(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
        return LooseWorkNumberMetricDTO.builder()
                .looseWorkNumber(allMarketHiddenMetrics.stream()
                                         .filter(t -> Objects.nonNull(t.getMetricStruct()))
                                         .map(t -> t.getMetricStruct().getNumber())
                                         .reduce(Long::sum).orElse(0L))
                .build();
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

    /**
     * 小散工程完成率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_COMPLETED_RATE", description = "小散工程完成率指标")
    public LooseWorkCompletedRateMetricDTO projectCompletedRate(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
        List<MarketMetricResult<ProjectScheduleMetricVO>> completedMarketHiddenMetrics = getMetricType(param, MetricType.COMPLETED_RATE);
        Map<Long, ProjectScheduleMetricVO> projectScheduleMetricVOMap = completedMarketHiddenMetrics.stream()
                .collect(Collectors.toMap(MarketMetricResult::getMarketId, MarketMetricResult::getMetricStruct,
                                          (o1, o2) -> o1));
        Long totalNum = 0L;
        Long completedNum = 0L;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<ProjectScheduleMetricVO> marketMetricResult : allMarketHiddenMetrics) {
            totalNum = totalNum + marketMetricResult.getMetricStruct().getNumber();
            Long marketCompletedNum = 0L;
            ProjectScheduleMetricVO metricVOMapOrDefault = projectScheduleMetricVOMap.getOrDefault(marketMetricResult.getMarketId(), new ProjectScheduleMetricVO());
            if (projectScheduleMetricVOMap.containsKey(marketMetricResult.getMarketId())) {
                marketCompletedNum = projectScheduleMetricVOMap.get(marketMetricResult.getMarketId()).getNumber();
            }
            completedNum = completedNum + (Objects.nonNull(metricVOMapOrDefault.getNumber()) ? metricVOMapOrDefault.getNumber() : 0L);
            marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                   .marketName(marketMetricResult.getMarketName())
                                                   .number(marketCompletedNum)
                                                   .rate(Objects.nonNull(metricVOMapOrDefault.getRate()) ? metricVOMapOrDefault.getRate()
                                                                                                         : 0)
                                                   .build());

        }
        return LooseWorkCompletedRateMetricDTO.builder()
                .looseWorkTotalNumber(totalNum)
                .looseWorkCompletedNumber(completedNum)
                .looseWorkCompletedRate(
                        totalNum == 0L ?
                        0d : BigDecimal.valueOf(completedNum)
                                .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue())
                .topCompleteRateMarket(marketCompletedRateMetrics.stream()
                                               .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed())
                                               .limit(5).collect(Collectors.toList()))
                .bottomCompleteRateMarket(marketCompletedRateMetrics.stream()
                                                  .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate))
                                                  .limit(5).collect(Collectors.toList()))
                .build();
    }

    /**
     * 小散工程异常率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_ABNORMAL_RATE", description = "小散工程异常率指标")
    public LooseWorkAbnormalRateMetricDTO projectAbnormalRate(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
        List<MarketMetricResult<ProjectScheduleMetricVO>> abnormalMarketHiddenMetrics = getMetricType(param, MetricType.ABNORMAL_RATE);

        Long totalNum = allMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
        Long abnormalNum = abnormalMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
        return LooseWorkAbnormalRateMetricDTO.builder()
                .looseWorkAbnormalNumber(abnormalNum)
                .looseWorkAbnormalRate(BigDecimal.valueOf(abnormalNum)
                                               .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                                               .multiply(BigDecimal.valueOf(100))
                                               .doubleValue())
                .build();
    }

    /**
     * 小散工程整改率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_REFORM_RATE", description = "小散工程整改率指标")
    public LooseWorkReformRateMetricDTO projectReformRate(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_RATE);

        Long totalNum = allMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);
        Long reformNum = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getNumber())
                .reduce(Long::sum).orElse(0L);

        return LooseWorkReformRateMetricDTO.builder()
                .looseWorkReformNumber(reformNum)
                .looseWorkReformRate(BigDecimal.valueOf(reformNum)
                                             .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                                             .multiply(BigDecimal.valueOf(100))
                                             .doubleValue())
                .build();
    }

    /**
     * 小散工程整改效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_REFORM_EFFICIENCY", description = "小散工程整改效率指标")
    public LooseWorkReformEfficiencyMetricDTO projectReformEfficiency(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_EFFICIENCY);
        Long totalTime = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getAvgTime())
                .reduce(Long::sum).orElse(0L);
        long processTime = BigDecimal.valueOf(totalTime)
                .divide(BigDecimal.valueOf(reformMarketHiddenMetrics.size()), 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).longValue();

        return LooseWorkReformEfficiencyMetricDTO.builder()
                .looseWorkReformEfficiency((double) processTime)
                .build();
    }

    /**
     * 小散工程整改率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_REFORM_TOP_METRIC", description = "小散工程整改率排名指标")
    public HeadLoosWorksReformRateDTO projectReformTopMetric(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_RATE);
        List<HeadLoosWorksReformRateDTO.MarketReformRate> marketReformRateLastList = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t ->
                             HeadLoosWorksReformRateDTO.MarketReformRate.builder()
                                     .marketName(t.getMarketName())
                                     .reformNum(t.getMetricStruct().getNumber())
                                     .reformRate(t.getMetricStruct().getRate())
                                     .build())
                .sorted(Comparator.comparing(HeadLoosWorksReformRateDTO.MarketReformRate::getReformRate))
                .limit(3)
                .collect(Collectors.toList());
        List<HeadLoosWorksReformRateDTO.MarketReformRate> marketReformRateTopList = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t ->
                             HeadLoosWorksReformRateDTO.MarketReformRate.builder()
                                     .marketName(t.getMarketName())
                                     .reformNum(t.getMetricStruct().getNumber())
                                     .reformRate(t.getMetricStruct().getRate())
                                     .build())
                .sorted(Comparator.comparing(HeadLoosWorksReformRateDTO.MarketReformRate::getReformRate).reversed())
                .limit(3)
                .collect(Collectors.toList());
        return HeadLoosWorksReformRateDTO.builder()
                .marketReformRateTopThree(marketReformRateTopList)
                .marketReformRateLastThree(marketReformRateLastList)
                .build();
    }

    @MetricTopic(topic = "HEADQUARTERS_PROJECT_REFORM_EFFICIENCY_TOP_METRIC", description = "小散工程整改效率排名指标")
    public HeadLoosWorksReformEfficiencyMetricDTO projectReformEfficiencyTopMetric(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_EFFICIENCY);
        List<HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency> marketReformRateLastList = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t ->
                             HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency.builder()
                                     .marketName(t.getMarketName())
                                     .reformEfficiency(BigDecimal.valueOf(t.getMetricStruct().getAvgTime())
                                                               .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build())
                .sorted(Comparator.comparing(HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency::getReformEfficiency).reversed())
                .limit(3)
                .collect(Collectors.toList());
        List<HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency> marketReformRateTopList = reformMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t ->
                             HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency.builder()
                                     .marketName(t.getMarketName())
                                     .reformEfficiency(BigDecimal.valueOf(t.getMetricStruct().getAvgTime())
                                                               .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build())
                .sorted(Comparator.comparing(HeadLoosWorksReformEfficiencyMetricDTO.MarketReformEfficiency::getReformEfficiency))
                .limit(3)
                .collect(Collectors.toList());
        return HeadLoosWorksReformEfficiencyMetricDTO.builder()
                .reformEfficiencyTopThree(marketReformRateTopList)
                .reformEfficiencyTopThree(marketReformRateLastList)
                .build();
    }

    /**
     * 小散工程验收率
     */
    @MetricTopic(topic = "HEADQUARTERS_PROJECT_ACCEPTANCE_RATE", description = "总部小散工程验收率指标")
    public HeadquartersAcceptanceRateMetric headquartersProjectAcceptanceRate(MetricParam param) {
        List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.ACCEPTANCE_RATE);
        List<MarketCompletedRateMetric> marketCompletedRateMetricList = reformMarketHiddenMetrics.stream().map(t ->
                                                                                                                       MarketCompletedRateMetric
                                                                                                                               .builder()
                                                                                                                               .marketName(t.getMarketName())
                                                                                                                               .number(t
                                                                                                                                               .getMetricStruct()
                                                                                                                                               .getNumber())
                                                                                                                               .rate(t
                                                                                                                                             .getMetricStruct()
                                                                                                                                             .getRate())
                                                                                                                               .build()
                                                                                                              )
                .collect(Collectors.toList());
        HeadquartersAcceptanceRateMetric rateMetric = new HeadquartersAcceptanceRateMetric();
        rateMetric.setAcceptanceRate(
                marketCompletedRateMetricList.stream().map(MarketCompletedRateMetric::getRate).reduce(Double::sum).orElse(0D) /
                marketCompletedRateMetricList.size());
        rateMetric.setAcceptanceNumber(marketCompletedRateMetricList.stream().map(MarketCompletedRateMetric::getNumber).reduce(Long::sum)
                                               .orElse(0L));
        rateMetric.setTopAcceptRateMarket(marketCompletedRateMetricList.stream()
                                                  .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed())
                                                  .limit(3).collect(Collectors.toList()));
        rateMetric.setBottomAcceptRateMarket(marketCompletedRateMetricList.stream()
                                                     .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate))
                                                     .limit(3).collect(Collectors.toList()));
        return rateMetric;
    }

    public List<ProjectScheduleAllMetricDTO> getMetric(Period period, MetricType metricType) {
        List<ProjectScheduleAllMetricDTO> projectScheduleAllMetricList = new ArrayList<>();
        MetricParam param = new MetricParam();
        param.setPeriod(period);
        if (Objects.isNull(metricType)) {
            getMetricByType(MetricType.ALL, param, projectScheduleAllMetricList);
        } else {
            getMetricByType(metricType, param, projectScheduleAllMetricList);
        }
        return projectScheduleAllMetricList;
    }

    private void getMetricByType(MetricType value, MetricParam param, List<ProjectScheduleAllMetricDTO> projectScheduleAllMetricList) {
        switch (value) {
            case TOTAL_COUNT:
                List<MarketMetricResult<ProjectScheduleMetricVO>> allMarketHiddenMetrics = getMetricType(param, MetricType.TOTAL_COUNT);
                allMarketHiddenMetrics.forEach(t ->
                                                       projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                                .marketName(t.getMarketName())
                                                                                                .projectCount(t.getMetricStruct()
                                                                                                                      .getTotalNumber())
                                                                                                .build()));
                break;
            case COMPLETED_RATE:
                List<MarketMetricResult<ProjectScheduleMetricVO>> completedMarketHiddenMetrics = getMetricType(param, MetricType.COMPLETED_RATE);
                completedMarketHiddenMetrics.forEach(t ->
                                                             projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                                      .marketName(t.getMarketName())
                                                                                                      .projectCount(t.getMetricStruct()
                                                                                                                            .getTotalNumber())
                                                                                                      .completeRate(t.getMetricStruct()
                                                                                                                            .getRate())
                                                                                                      .build()));
                break;
            case ABNORMAL_RATE:
                List<MarketMetricResult<ProjectScheduleMetricVO>> abnormalMarketHiddenMetrics = getMetricType(param, MetricType.ABNORMAL_RATE);
                abnormalMarketHiddenMetrics.forEach(t ->
                                                            projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                                     .marketName(t.getMarketName())
                                                                                                     .projectCount(t.getMetricStruct()
                                                                                                                           .getTotalNumber())
                                                                                                     .abnormalRate(t.getMetricStruct()
                                                                                                                           .getRate())
                                                                                                     .build()));
                break;
            case REFORM_RATE:
                List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenMetrics = getMetricType(param, MetricType.REFORM_RATE);
                reformMarketHiddenMetrics.forEach(t ->
                                                          projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                                   .marketName(t.getMarketName())
                                                                                                   .projectCount(t.getMetricStruct()
                                                                                                                         .getTotalNumber())
                                                                                                   .rectificationRate(t.getMetricStruct()
                                                                                                                              .getRate())
                                                                                                   .build()));
                break;
            case REFORM_EFFICIENCY:
                List<MarketMetricResult<ProjectScheduleMetricVO>> reformMarketHiddenEfficiencyMetrics = getMetricType(param, MetricType.REFORM_EFFICIENCY);
                reformMarketHiddenEfficiencyMetrics.forEach(t ->
                                                                    projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                                             .marketName(t.getMarketName())
                                                                                                             .rectificationEfficiency(Double.valueOf(t
                                                                                                                                                             .getMetricStruct()
                                                                                                                                                             .getAvgTime()))
                                                                                                             .build()));
                break;
            case ACCEPTANCE_RATE:
                List<MarketMetricResult<ProjectScheduleMetricVO>> acceptanceRateMetric = getMetricType(param, MetricType.ACCEPTANCE_RATE);
                acceptanceRateMetric.forEach(t ->
                                                     projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                                                              .marketName(t.getMarketName())
                                                                                              .acceptanceRate(t.getMetricStruct().getRate())
                                                                                              .build()));
                break;
            case ALL:
                List<MarketMetricResult<ProjectScheduleMetricVO>> completedList = getMetricType(param, MetricType.COMPLETED_RATE);
                List<MarketMetricResult<ProjectScheduleMetricVO>> abnormalList = getMetricType(param, MetricType.ABNORMAL_RATE);
                Map<Long, MarketMetricResult<ProjectScheduleMetricVO>> abnormalMap = abnormalList.stream()
                        .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
                List<MarketMetricResult<ProjectScheduleMetricVO>> reformList = getMetricType(param, MetricType.REFORM_RATE);
                Map<Long, MarketMetricResult<ProjectScheduleMetricVO>> reformMap = abnormalList.stream()
                        .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
                List<MarketMetricResult<ProjectScheduleMetricVO>> reformEfficeiencyList = getMetricType(param, MetricType.REFORM_EFFICIENCY);
                Map<Long, MarketMetricResult<ProjectScheduleMetricVO>> reformEfficeiencyMap = abnormalList.stream()
                        .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
                List<MarketMetricResult<ProjectScheduleMetricVO>> acceptanceList = getMetricType(param, MetricType.ACCEPTANCE_RATE);
                Map<Long, MarketMetricResult<ProjectScheduleMetricVO>> acceptanceMap = abnormalList.stream()
                        .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
                completedList.forEach(t -> {
                    MarketMetricResult<ProjectScheduleMetricVO> abnormal = abnormalMap.get(t.getMarketId());
                    MarketMetricResult<ProjectScheduleMetricVO> reform = reformMap.get(t.getMarketId());
                    MarketMetricResult<ProjectScheduleMetricVO> reformEfficiency = reformEfficeiencyMap.get(t.getMarketId());
                    MarketMetricResult<ProjectScheduleMetricVO> acceptance = acceptanceMap.get(t.getMarketId());
                    projectScheduleAllMetricList.add(ProjectScheduleAllMetricDTO.builder()
                                                             .marketName(t.getMarketName())
                                                             .projectCount(t.getMetricStruct().getTotalNumber())
                                                             .completeRate(t.getMetricStruct().getRate())
                                                             .abnormalRate(abnormal == null ? 0 : abnormal.getMetricStruct().getRate())
                                                             .rectificationRate(reform == null ? 0 : reform.getMetricStruct().getRate())
                                                             .rectificationEfficiency(reformEfficiency == null ? 0d
                                                                                                               : Double.valueOf(reformEfficiency
                                                                                                                                        .getMetricStruct()
                                                                                                                                        .getAvgTime()))
                                                             .acceptanceRate(
                                                                     acceptance == null ? 0 : acceptance.getMetricStruct().getRate())
                                                             .build());
                });

                break;
            default:
                break;
        }
    }
}
