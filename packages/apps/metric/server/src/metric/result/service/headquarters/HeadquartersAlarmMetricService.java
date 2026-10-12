package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmBimStatisticsHandlerVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelTopStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.McpAlarmQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmFlameSmogHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmFlameSmogNumMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadFsHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadFsMarketHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadFsNumberTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadFsUserHandlerEfficiencyTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadFsUserHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadHandlerTimeoutRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadMarketHandlerEfficiencyTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadMarketHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadOneLevelMarketHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadOneLevelMarketNumTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadOneLevelUserHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadUserHandlerEfficiencyTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.HeadUserHandlerRateTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketAlarmNum;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketHandlerDuration;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketHandlerRate;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketTimeoutHandlerRate;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketUserHandlerDuration;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketUserHandlerRateDetails;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm.MarketUserTimeoutHandlerRate;
import com.guhecloud.cube.biz.metric.server.metric.service.headquarters.dto.alarm.MarketHandlerRateLevel;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.controller.McpController.defautlPeriod;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.FlAME_SMOG_EVENT_CODES;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.HANDLER_STATUS_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.ILOR_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.LCA_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.MCP_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.ONE_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TC_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TEMPERATURE_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.THREE_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.TWO_LEVEL_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.UPFL_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.UPS_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.VAL_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.WP_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.WSD_EVENT_CODE;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_EVENT_TYPE_NUM_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_HANDLER_EFFICIENCY_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_LEVEL_AVG_HANDLER_EFFICIENCY_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_LEVEL_HANDLER_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_LEVEL_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_LEVEL_STATISTICS_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_LEVEL_USER_HANDLER_EFFICIENCY_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_STATUS_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_EFFICIENCY_LAST_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_EFFICIENCY_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_RATE_LAST_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.ALARM_USER_HANDLER_TIMEOUT_RATE_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 11:05
 * @Description:
 */
@Service
public class HeadquartersAlarmMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadquartersAlarmMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    /**
     * 报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_NUMBER", description = "报警总数指标")
    public AlarmNumberMetricDTO getAlarmNumberMetric(MetricParam param) {
        Long totalNum = this.getAlarmNumberByEventType(null, this.convertQueryParm(param));
        return AlarmNumberMetricDTO.builder()
                .alarmNumber(totalNum.intValue())
                .build();
    }

    /**
     * 按级别统计报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_LEVEL_NUMBER", description = "报警级别数量指标")
    public List<AlarmLevelNumberMetricDTO> getAlarmLevelStatistics(MetricParam param) {

        //总数
        Long totalNum = this.getAlarmNumberByEventType(null, this.convertQueryParm(param));
        List<MarketMetricResult<List<AlarmLevelStatisticsVO>>> allAlarmLevelMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_NUMBER_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param), new TypeReference<List<AlarmLevelStatisticsVO>>() {
                });

        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = allAlarmLevelMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Map<String, List<AlarmLevelStatisticsVO>> groupByCodeMap = alarmLevelStatisticsVOS.stream()
                .collect(Collectors.groupingBy(AlarmLevelStatisticsVO::getCode));
        List<AlarmLevelNumberMetricDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<AlarmLevelStatisticsVO>> entry : groupByCodeMap.entrySet()) {
            AlarmLevelStatisticsVO alarmLevelStatisticsVO = entry.getValue().get(0);
            Long alarmNum = entry.getValue().stream().map(AlarmLevelStatisticsVO::getAlarmNum)
                    .reduce(Long::sum).orElse(0L);
            AlarmLevelNumberMetricDTO metricDTO = AlarmLevelNumberMetricDTO.builder()
                    .code(alarmLevelStatisticsVO.getCode().toUpperCase())
                    .name(alarmLevelStatisticsVO.getName())
                    .alarmNum(alarmNum)
                    .build();
            double ratio = 0;
            if (0 == totalNum.intValue()) {
                ratio = 0.0;
            } else {
                ratio = BigDecimal.valueOf(alarmLevelStatisticsVO.getAlarmNum())
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }
            metricDTO.setRatio(ratio);
            result.add(metricDTO);
        }
        return result;
    }

    /**
     * 市场一级报警数排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_LEVEL_NUMBER_MARKET_TOP", description = "市场一级报警数排名指标")
    public HeadOneLevelMarketNumTopMetricDTO getAlarmOneLevelMarketTopStatistics(MetricParam param) {
        List<MarketAlarmNum> marketMetricResults = getOneLevelMarketAlarmNums(this.convertQueryParm(param), ONE_LEVEL_CODE);
        List<MarketAlarmNum> highMarkets = marketMetricResults.stream()
                .sorted(Comparator.comparing(MarketAlarmNum::getAlarmNumber).reversed())
                .limit(3).collect(Collectors.toList());
        List<MarketAlarmNum> lowMarkets = marketMetricResults.stream().sorted(Comparator.comparing(MarketAlarmNum::getAlarmNumber)).limit(3)
                .collect(Collectors.toList());
        return HeadOneLevelMarketNumTopMetricDTO.builder()
                .oneLevelAlarmNumTopMarkets(highMarkets)
                .oneLevelAlarmNumLowMarkets(lowMarkets)
                .build();
    }

    private List<MarketAlarmNum> getOneLevelMarketAlarmNums(AlarmStatisticsQueryParam param, String levelCode) {
        List<MarketMetricResult<List<AlarmLevelStatisticsVO>>> allAlarmLevelMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<List<AlarmLevelStatisticsVO>>() {
                });
        List<MarketAlarmNum> marketMetricResults = new ArrayList<>();
        for (MarketMetricResult<List<AlarmLevelStatisticsVO>> allAlarmLevelMarketMetric : allAlarmLevelMarketMetrics) {
            MarketAlarmNum marketAlarmNum = new MarketAlarmNum();
            marketAlarmNum.setMarketName(allAlarmLevelMarketMetric.getMarketName());
            if (CollectionUtils.isNotEmpty(allAlarmLevelMarketMetric.getMetricStruct()) ||
                allAlarmLevelMarketMetric.getMetricStruct().stream()
                        .noneMatch(result -> levelCode.equals(result.getCode()))) {
                marketAlarmNum.setAlarmNumber(0);
            } else {
                AlarmLevelStatisticsVO oneLevel = allAlarmLevelMarketMetric.getMetricStruct().stream()
                        .filter(result -> levelCode.equals(result.getCode())).findFirst().get();
                marketAlarmNum.setAlarmNumber(oneLevel.getAlarmNum().intValue());
            }
            marketMetricResults.add(marketAlarmNum);
        }
        return marketMetricResults;
    }

    private AlarmStatisticsQueryParam convertQueryParm(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        return queryParam;
    }

    /**
     * 报警总处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_HANDLER_RATE", description = "报警总处理率指标")
    public AlarmHandlerRateMetricDTO getAlarmHandlerRateMetric(MetricParam param) {
        Long totalNum = this.getAlarmNumberByEventType(null, this.convertQueryParm(param));
        if (0 == totalNum) {
            return new AlarmHandlerRateMetricDTO(0, 0.0);
        }
        Optional<AlarmStatusStatisticsVO> handlerOpt = getAlarmHandlerNumber(null, param);
        if (!handlerOpt.isPresent() || 0 == handlerOpt.get().getAlarmNum()) {
            return new AlarmHandlerRateMetricDTO(0, 0.0);
        }
        return AlarmHandlerRateMetricDTO.builder()
                .alarmHandlerNumber(handlerOpt.get().getAlarmNum().intValue())
                .alarmHandlerRate(BigDecimal.valueOf(handlerOpt.get().getAlarmNum())
                                          .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                                          .multiply(BigDecimal.valueOf(100))
                                          .doubleValue())
                .build();
    }

    private Optional<AlarmStatusStatisticsVO> getAlarmHandlerNumber(Set<String> alarmEventTypeIds, MetricParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertQueryParm(param);
        queryParam.setAlarmEventTypeIds(alarmEventTypeIds);
        List<MarketMetricResult<List<AlarmStatusStatisticsVO>>> allMarketInspectionTotalMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<List<AlarmStatusStatisticsVO>>() {
                });

        List<AlarmStatusStatisticsVO> alarmStatusStatisticsVOS = allMarketInspectionTotalMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Optional<AlarmStatusStatisticsVO> handlerOpt = alarmStatusStatisticsVOS.stream()
                .filter(status -> "ALARM_HANDLER"
                        .equals(status.getAlarmStatus()))
                .findFirst();
        return handlerOpt;
    }

    /**
     * 报警级别处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_LEVEL_HANDLER_RATE", description = "报警级别处理率指标")
    public List<AlarmLevelHandlerRateMetricDTO> getAlarmLevelHandlerRateMetric(MetricParam param) {
        List<MarketMetricResult<List<AlarmLevelStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_HANDLER_RATE_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmLevelStatisticsVO>>() {
                                     });
        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = allMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Map<String, List<AlarmLevelStatisticsVO>> groupLevelMap = alarmLevelStatisticsVOS.stream()
                .collect(Collectors.groupingBy(AlarmLevelStatisticsVO::getCode));
        List<AlarmLevelHandlerRateMetricDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<AlarmLevelStatisticsVO>> entry : groupLevelMap.entrySet()) {
            AlarmLevelStatisticsVO alarmLevelStatisticsVO = entry.getValue().get(0);
            Long alarmNum = entry.getValue().stream().map(AlarmLevelStatisticsVO::getAlarmNum)
                    .reduce(Long::sum).orElse(0L);
            Long handlerNum = entry.getValue().stream().map(AlarmLevelStatisticsVO::getHandlerNum)
                    .reduce(Long::sum).orElse(0L);
            AlarmLevelHandlerRateMetricDTO metricDTO = AlarmLevelHandlerRateMetricDTO.builder()
                    .code(alarmLevelStatisticsVO.getCode().toUpperCase())
                    .name(alarmLevelStatisticsVO.getName())
                    .alarmNum(alarmNum)
                    .handlerNum(handlerNum)
                    .handlerRate(BigDecimal.valueOf(handlerNum)
                                         .divide(BigDecimal.valueOf(alarmNum), 2, RoundingMode.HALF_UP)
                                         .multiply(BigDecimal.valueOf(100))
                                         .floatValue())
                    .build();
            result.add(metricDTO);
        }
        return result;
    }

    /**
     * 报警总处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_HANDLER_EFFICIENCY", description = "报警总处理效率指标")
    public AlarmHandlerEfficiencyMetricDTO getAlarmHandlerEfficiencyMetric(MetricParam param) {
        List<MarketMetricResult<AlarmAvgHandlerEfficiencyVO>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<AlarmAvgHandlerEfficiencyVO>() {
                                     });
        Float totalHandlerDuration = allMarketMetrics.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getAvgHandlerDuration())
                .reduce(Float::sum).orElse(0f);
        return AlarmHandlerEfficiencyMetricDTO.builder()
                .alarmAvgHandlerDuration(BigDecimal.valueOf(totalHandlerDuration)
                                                 .divide(BigDecimal.valueOf(allMarketMetrics.size()), 2, RoundingMode.HALF_UP)
                                                 .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    /**
     * 报警级别处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_LEVEL_HANDLER_EFFICIENCY", description = "报警分级处理效率指标")
    public List<AlarmLevelHandlerEfficiencyMetricDTO> getAlarmLevelHandlerEfficiencyMetric(MetricParam param) {
        List<MarketMetricResult<List<AlarmLevelHandlerEfficiencyStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_AVG_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmLevelHandlerEfficiencyStatisticsVO>>() {
                                     });
        List<AlarmLevelHandlerEfficiencyStatisticsVO> alarmLevelHandlerEfficiencyStatisticsVOS = allMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Map<String, List<AlarmLevelHandlerEfficiencyStatisticsVO>> levelHandlerEfficiencyMap = alarmLevelHandlerEfficiencyStatisticsVOS
                .stream()
                .collect(Collectors.groupingBy(AlarmLevelHandlerEfficiencyStatisticsVO::getCode));
        List<AlarmLevelHandlerEfficiencyMetricDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<AlarmLevelHandlerEfficiencyStatisticsVO>> entry : levelHandlerEfficiencyMap.entrySet()) {
            AlarmLevelHandlerEfficiencyStatisticsVO alarmLevelEfficiency = entry.getValue().get(0);
            AlarmLevelHandlerEfficiencyMetricDTO efficiencyMetricDTO = AlarmLevelHandlerEfficiencyMetricDTO.builder()
                    .code(alarmLevelEfficiency.getCode().toUpperCase())
                    .name(alarmLevelEfficiency.getName())
                    .build();
            Long handlerNum = entry.getValue().stream().map(AlarmLevelHandlerEfficiencyStatisticsVO::getHandlerNum)
                    .reduce(Long::sum).orElse(0L);
            Long durationTotal = entry.getValue().stream().map(AlarmLevelHandlerEfficiencyStatisticsVO::getDurationTotal)
                    .reduce(Long::sum).orElse(0L);
            Float avgHandlerDuration = entry.getValue().stream().map(AlarmLevelHandlerEfficiencyStatisticsVO::getAvgHandlerDuration)
                    .reduce(Float::sum).orElse(0f);
            efficiencyMetricDTO.setHandlerNum(handlerNum);
            efficiencyMetricDTO.setDurationTotal(durationTotal);
            efficiencyMetricDTO.setAvgHandlerDuration(BigDecimal.valueOf(avgHandlerDuration)
                                                              .divide(BigDecimal.valueOf(allMarketMetrics.size()), 2, RoundingMode.HALF_UP)
                                                              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).floatValue());
            result.add(efficiencyMetricDTO);
        }
        return result;
    }

    /**
     * 明火烟雾报警数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_NUMBER", description = "明火烟雾报警数量指标")
    public AlarmFlameSmogNumMetricDTO getFlameSmogNumMetric(MetricParam param) {
        Long totalNum = this.getAlarmNumberByEventType(null, this.convertQueryParm(param));
        if (0 == totalNum) {
            return new AlarmFlameSmogNumMetricDTO(0, 0.0);
        }
        Long sameSmogAlarmNum = this.getAlarmNumberByEventType(FlAME_SMOG_EVENT_CODES, this.convertQueryParm(param));
        return new AlarmFlameSmogNumMetricDTO(sameSmogAlarmNum.intValue(),
                                              BigDecimal.valueOf(sameSmogAlarmNum)
                                                      .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                                                      .multiply(BigDecimal.valueOf(100))
                                                      .doubleValue());
    }

    /**
     * 市场明火烟雾数量排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_MARKET_FLAME_SMOG_NUMBER_TOP", description = "市场明火烟雾数量排名指标")
    public HeadFsNumberTopMetricDTO getMarketFlameSmogNumTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertQueryParm(param);
        List<MarketMetricResult<AlarmBasicAnalysisStatisticsDTO>> allAlarmMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmBasicAnalysisStatisticsDTO>() {
                                     });
        List<MarketAlarmNum> alarmMarkets = allAlarmMarketMetrics.stream().filter(result -> Objects.nonNull(result.getMetricStruct()))
                .map(result ->
                             MarketAlarmNum.builder()
                                     .marketName(result.getMarketName())
                                     .alarmNumber(result.getMetricStruct().getAlarmNum().intValue())
                                     .build()
                    ).collect(Collectors.toList());
        List<MarketAlarmNum> highMarkets = alarmMarkets.stream().sorted(Comparator.comparing(MarketAlarmNum::getAlarmNumber).reversed())
                .limit(3).collect(Collectors.toList());
        List<MarketAlarmNum> lowMarkets = alarmMarkets.stream().sorted(Comparator.comparing(MarketAlarmNum::getAlarmNumber)).limit(3)
                .collect(Collectors.toList());
        return HeadFsNumberTopMetricDTO.builder()
                .flameSmogHighMarkets(highMarkets)
                .flameSmogLowMarkets(lowMarkets)
                .build();
    }

    /**
     * 明火烟雾报警处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_HANDLER_RATE", description = "明火烟雾报警处理率指标")
    public AlarmFlameSmogHandlerRateMetricDTO getFlameSmogHandlerRateMetric(MetricParam param) {
        Long sameSmogAlarmNum = this.getAlarmNumberByEventType(FlAME_SMOG_EVENT_CODES, this.convertQueryParm(param));
        if (0 == sameSmogAlarmNum) {
            return new AlarmFlameSmogHandlerRateMetricDTO(0, 0.0);
        }
        Optional<AlarmStatusStatisticsVO> handlerOpt = getAlarmHandlerNumber(FlAME_SMOG_EVENT_CODES, param);
        if (!handlerOpt.isPresent() || 0 == handlerOpt.get().getAlarmNum()) {
            return new AlarmFlameSmogHandlerRateMetricDTO(0, 0.0);
        }
        return AlarmFlameSmogHandlerRateMetricDTO.builder()
                .flameSmogAlarmHandlerNumber(handlerOpt.get().getAlarmNum().intValue())
                .flameSmogAlarmHandlerRate(BigDecimal.valueOf(handlerOpt.get().getAlarmNum())
                                                   .divide(BigDecimal.valueOf(sameSmogAlarmNum), 2, RoundingMode.HALF_UP)
                                                   .multiply(BigDecimal.valueOf(100))
                                                   .doubleValue())
                .build();
    }

    /**
     * 市场明火烟雾报警处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_MARKET_HANDLER_RATE_TOP", description = "市场明火烟雾报警处理率排名指标")
    public HeadFsMarketHandlerRateTopMetricDTO getFlameSmogMarketHandlerRateTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        HeadMarketHandlerRateTopMetricDTO headMarketHandlerRateTopMetricDTO = this.getHeadMarketHandlerRateTopMetricDTO(queryParam);
        return HeadFsMarketHandlerRateTopMetricDTO.builder()
                .topFlameSmogHandlerMarkets(headMarketHandlerRateTopMetricDTO.getTopHandlerMarkets())
                .lowFlameSmogHandlerMarkets(headMarketHandlerRateTopMetricDTO.getLowHandlerMarkets())
                .build();
    }

    /**
     * 用户明火烟雾报警处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_USER_HANDLER_RATE_TOP", description = "用户明火烟雾报警处理率排名指标")
    public HeadFsUserHandlerRateTopMetricDTO getFlameSmogUserHandlerRateTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        HeadUserHandlerRateTopMetricDTO alarmMarketUserHandlerRateTopMetricDTO = getAlarmMarketUserHandlerRateTopMetricDTO(queryParam);
        return HeadFsUserHandlerRateTopMetricDTO.builder()
                .topFlameSmogHandlerUsers(alarmMarketUserHandlerRateTopMetricDTO.getTopHandlerUsers())
                .lowFlameSmogHandlerUsers(alarmMarketUserHandlerRateTopMetricDTO.getLowHandlerUsers())
                .build();
    }

    /**
     * 明火烟雾报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_HANDLER_EFFICIENCY", description = "明火烟雾报警处理效率指标")
    public HeadFsHandlerEfficiencyMetricDTO getFlameSmogHandlerEfficiencyMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertQueryParm(param);
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        List<MarketMetricResult<AlarmAvgHandlerEfficiencyVO>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmAvgHandlerEfficiencyVO>() {
                                     });
        Float totalHandlerDuration = allMarketMetrics.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getAvgHandlerDuration())
                .reduce(Float::sum).orElse(0f);
        Optional<MarketMetricResult<AlarmAvgHandlerEfficiencyVO>> max = allMarketMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .max(Comparator.comparing(t -> t.getMetricStruct().getMaxHandlerDuration()));
        Optional<MarketMetricResult<AlarmAvgHandlerEfficiencyVO>> min = allMarketMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .min(Comparator.comparing(t -> t.getMetricStruct().getMinHandlerDuration()));
        return HeadFsHandlerEfficiencyMetricDTO.builder()
                .flameSmogAlarmAvgHandlerDuration(BigDecimal.valueOf(totalHandlerDuration)
                                                          .divide(BigDecimal.valueOf(allMarketMetrics.size()), 2, RoundingMode.HALF_UP)
                                                          .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .maxHandlerDurationMarket(
                        MarketHandlerDuration.builder()
                                .marketName(max.get().getMarketName())
                                .handlerDuration(BigDecimal.valueOf(max.get().getMetricStruct().getMaxHandlerDuration())
                                                         .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                .build())
                .minHandlerDurationMarket(MarketHandlerDuration.builder()
                                                  .marketName(min.get().getMarketName())
                                                  .handlerDuration(BigDecimal.valueOf(min.get().getMetricStruct().getMaxHandlerDuration())
                                                                           .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                                           .doubleValue())
                                                  .build())
                .build();
    }

    /**
     * 明火烟雾报警用户处理效率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_FLAME_SMOG_USER_HANDLER_EFFICIENCY", description = "明火烟雾报警用户处理效率指标")
    public HeadFsUserHandlerEfficiencyTopMetricDTO getFlameSmogUserHandlerEfficiencyTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertQueryParm(param);
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        HeadUserHandlerEfficiencyTopMetricDTO headAlarmUserHandlerEfficiencyTopMetricDTO = getHeadAlarmUserHandlerEfficiencyTopMetricDTO(queryParam);
        return HeadFsUserHandlerEfficiencyTopMetricDTO.builder()
                .topFlameSmogHandlerUsers(headAlarmUserHandlerEfficiencyTopMetricDTO.getTopHandlerUsers())
                .lowFlameSmogHandlerUsers(headAlarmUserHandlerEfficiencyTopMetricDTO.getLowHandlerUsers())
                .build();
    }

    /**
     * 报警类型数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_EVENT_TYPE_NUM", description = "报警类型处理效率指标")
    public List<AlarmEventTypeNumberMetricDTO> getAlarmEventTypeNumberMetric(MetricParam param) {

        List<MarketMetricResult<List<AlarmBimStatisticsHandlerVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_EVENT_TYPE_NUM_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmBimStatisticsHandlerVO>>() {
                                     });
        List<AlarmBimStatisticsHandlerVO> alarmLevelStatisticsVOS = allMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Map<String, List<AlarmBimStatisticsHandlerVO>> groupByEventTypeMap = alarmLevelStatisticsVOS.stream()
                .collect(Collectors.groupingBy(AlarmBimStatisticsHandlerVO::getId));
        List<AlarmEventTypeNumberMetricDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<AlarmBimStatisticsHandlerVO>> entry : groupByEventTypeMap.entrySet()) {
            AlarmBimStatisticsHandlerVO alarmBimStatisticsHandlerVO = entry.getValue().get(0);
            Integer totalNum = entry.getValue().stream().map(AlarmBimStatisticsHandlerVO::getTotalNum)
                    .reduce(Integer::sum).orElse(0);
            result.add(AlarmEventTypeNumberMetricDTO.builder()
                               .alarmTypeCode(alarmBimStatisticsHandlerVO.getId())
                               .alarmTypeName(alarmBimStatisticsHandlerVO.getName())
                               .alarmTypeNumber(totalNum)
                               .build());
        }

        return result;
    }

    /**
     * 报警类型处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_EVENT_TYPE_HANDLER_RATE", description = "报警类型处理率指标")
    public List<AlarmEventTypeHandlerRateMetricDTO> getAlarmEventTypeHandlerRateMetric(MetricParam param) {
        List<MarketMetricResult<List<AlarmBimStatisticsHandlerVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_EVENT_TYPE_NUM_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmBimStatisticsHandlerVO>>() {
                                     });
        List<AlarmBimStatisticsHandlerVO> alarmLevelStatisticsVOS = allMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Map<String, List<AlarmBimStatisticsHandlerVO>> groupByEventTypeMap = alarmLevelStatisticsVOS.stream()
                .collect(Collectors.groupingBy(AlarmBimStatisticsHandlerVO::getId));
        List<AlarmEventTypeHandlerRateMetricDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<AlarmBimStatisticsHandlerVO>> entry : groupByEventTypeMap.entrySet()) {
            AlarmBimStatisticsHandlerVO alarmBimStatisticsHandlerVO = entry.getValue().get(0);
            Integer totalNum = entry.getValue().stream().map(AlarmBimStatisticsHandlerVO::getTotalNum)
                    .reduce(Integer::sum).orElse(0);
            Integer handlerNum = entry.getValue().stream().map(AlarmBimStatisticsHandlerVO::getHandlerNum)
                    .reduce(Integer::sum).orElse(0);
            double rate = 0;
            if (0 < totalNum) {
                rate = BigDecimal.valueOf(handlerNum)
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }
            result.add(AlarmEventTypeHandlerRateMetricDTO.builder()
                               .alarmTypeCode(alarmBimStatisticsHandlerVO.getId())
                               .alarmTypeName(alarmBimStatisticsHandlerVO.getName())
                               .alarmTypeNumber(totalNum)
                               .alarmTypeHandlerNumber(handlerNum)
                               .alarmTypeHandlerRate(rate)
                               .build());
        }
        return result;
    }

    /**
     * 市场处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_MARKET_HANDLER_RATE_TOP", description = "市场处理率排名指标")
    public HeadMarketHandlerRateTopMetricDTO getMarketHandlerRateTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        return getHeadMarketHandlerRateTopMetricDTO(queryParam);
    }

    /**
     * 用户处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_USER_HANDLER_RATE_TOP", description = "用户处理率排名指标")
    public HeadUserHandlerRateTopMetricDTO getUserHandlerRateTopMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPageSize(3);
        return getAlarmMarketUserHandlerRateTopMetricDTO(queryParam);
    }

    /**
     * 市场处理效率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_MARKET_HANDLER_EFFICIENCY_TOP_METRIC", description = "市场处理效率排名指标")
    public HeadMarketHandlerEfficiencyTopMetricDTO getMarketHandlerEfficiencyMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPageSize(3);
        return getHeadAlarmMarketHandlerEfficiencyTopMetricDTO(queryParam);
    }

    /**
     * 用户处理效率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_USER_HANDLER_EFFICIENCY_TOP_METRIC", description = "用户处理效率排名指标")
    public HeadUserHandlerEfficiencyTopMetricDTO getUserHandlerEfficiencyMetric(MetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPageSize(3);
        return getHeadAlarmUserHandlerEfficiencyTopMetricDTO(queryParam);
    }

    /**
     * 一级报警市场处理效率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ONE_LEVEL_ALARM_MARKET_HANDLER_EFFICIENCY_TOP_METRIC", description = "一级报警市场处理效率指标排名")
    public HeadOneLevelMarketHandlerEfficiencyMetricDTO getOneLevelMarketHandlerEfficiencyMetric(MetricParam param) {
        this.convertQueryParm(param);
        List<MarketMetricResult<List<AlarmLevelHandlerEfficiencyStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_USER_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmLevelHandlerEfficiencyStatisticsVO>>() {
                                     });
        MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> highOneLevelMarket = null;
        MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> lowOneLevelMarket = null;
        for (MarketMetricResult<List<AlarmLevelHandlerEfficiencyStatisticsVO>> allMarketMetric : allMarketMetrics) {
            if (allMarketMetric.getMetricStruct().stream().noneMatch(level -> ONE_LEVEL_CODE.equals(level.getCode()))) {
                continue;
            }
            AlarmLevelHandlerEfficiencyStatisticsVO alarmLevelHandlerEfficiencyStatisticsVO = allMarketMetric.getMetricStruct()
                    .stream().filter(level -> ONE_LEVEL_CODE.equals(level.getCode())).findFirst().get();
            if (Objects.isNull(highOneLevelMarket)) {
                MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> currentHighOneLevelMarket = new MarketMetricResult<>();
                currentHighOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                currentHighOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                currentHighOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                currentHighOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
                highOneLevelMarket = currentHighOneLevelMarket;
            } else if (highOneLevelMarket.getMetricStruct().getAvgHandlerDuration() >
                       alarmLevelHandlerEfficiencyStatisticsVO.getAvgHandlerDuration()) {
                highOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                highOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                highOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                highOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
            }
            if (Objects.isNull(lowOneLevelMarket)) {
                MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> currentLowOneLevelMarket = new MarketMetricResult<>();
                currentLowOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                currentLowOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                currentLowOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                currentLowOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
                lowOneLevelMarket = currentLowOneLevelMarket;
            } else if (lowOneLevelMarket.getMetricStruct().getAvgHandlerDuration() <
                       alarmLevelHandlerEfficiencyStatisticsVO.getAvgHandlerDuration()) {
                lowOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                lowOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                lowOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                lowOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
            }
        }
        if (Objects.isNull(highOneLevelMarket)) {
            return null;
        }
        return HeadOneLevelMarketHandlerEfficiencyMetricDTO.builder()
                .code(ONE_LEVEL_CODE)
                .name("一级")
                .handlerHighEfficiencyMarketName(highOneLevelMarket.getMetricStruct().getHighEfficiencyUserName())
                .handlerHighEfficiencyMarketAvgDuration(BigDecimal.valueOf(highOneLevelMarket.getMetricStruct().getAvgHandlerDuration())
                                                                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .handlerLowEfficiencyMarketName(lowOneLevelMarket.getMetricStruct().getLowEfficiencyUserName())
                .handlerLowEfficiencyMarketAvgDuration(BigDecimal.valueOf(lowOneLevelMarket.getMetricStruct()
                                                                                  .getLowEfficiencyUserAvgDuration())
                                                               .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    /**
     * 一级报警用户处理效率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ONE_LEVEL_ALARM_USER_HANDLER_EFFICIENCY_TOP_METRIC", description = "一级报警用户处理效率指标排名")
    public HeadOneLevelUserHandlerEfficiencyMetricDTO getOneLevelUserHandlerEfficiencyMetric(MetricParam param) {
        this.convertQueryParm(param);
        List<MarketMetricResult<List<AlarmLevelHandlerEfficiencyStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_USER_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, this.convertQueryParm(param),
                                     new TypeReference<List<AlarmLevelHandlerEfficiencyStatisticsVO>>() {
                                     });
        MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> highOneLevelMarket = null;
        MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> lowOneLevelMarket = null;
        for (MarketMetricResult<List<AlarmLevelHandlerEfficiencyStatisticsVO>> allMarketMetric : allMarketMetrics) {
            if (allMarketMetric.getMetricStruct().stream().noneMatch(level -> ONE_LEVEL_CODE.equals(level.getCode()))) {
                continue;
            }
            AlarmLevelHandlerEfficiencyStatisticsVO alarmLevelHandlerEfficiencyStatisticsVO = allMarketMetric.getMetricStruct()
                    .stream().filter(level -> ONE_LEVEL_CODE.equals(level.getCode())).findFirst().get();
            if (Objects.isNull(highOneLevelMarket)) {
                MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> currentHighOneLevelMarket = new MarketMetricResult<>();
                currentHighOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                currentHighOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                currentHighOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                currentHighOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
                highOneLevelMarket = currentHighOneLevelMarket;
            } else if (highOneLevelMarket.getMetricStruct().getHighEfficiencyUserAvgDuration() >
                       alarmLevelHandlerEfficiencyStatisticsVO.getHighEfficiencyUserAvgDuration()) {
                highOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                highOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                highOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                highOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
            }
            if (Objects.isNull(lowOneLevelMarket)) {
                MarketMetricResult<AlarmLevelHandlerEfficiencyStatisticsVO> currentLowOneLevelMarket = new MarketMetricResult<>();
                currentLowOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                currentLowOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                currentLowOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                currentLowOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
                lowOneLevelMarket = currentLowOneLevelMarket;
            } else if (lowOneLevelMarket.getMetricStruct().getHighEfficiencyUserAvgDuration() <
                       alarmLevelHandlerEfficiencyStatisticsVO.getLowEfficiencyUserAvgDuration()) {
                lowOneLevelMarket.setMarketCode(allMarketMetric.getMarketCode());
                lowOneLevelMarket.setMarketName(allMarketMetric.getMarketName());
                lowOneLevelMarket.setMarketTenant(allMarketMetric.getMarketTenant());
                lowOneLevelMarket.setMetricStruct(alarmLevelHandlerEfficiencyStatisticsVO);
            }
        }
        if (Objects.isNull(highOneLevelMarket)) {
            return null;
        }
        return HeadOneLevelUserHandlerEfficiencyMetricDTO.builder()
                .code(ONE_LEVEL_CODE)
                .name("一级")
                .handlerHighEfficiencyUserName(highOneLevelMarket.getMetricStruct().getHighEfficiencyUserName())
                .handlerHighEfficiencyUserAvgDuration(BigDecimal.valueOf(highOneLevelMarket.getMetricStruct()
                                                                                 .getHighEfficiencyUserAvgDuration())
                                                              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .handlerHighEfficiencyUserMarketName(highOneLevelMarket.getMarketName())
                .handlerLowEfficiencyUserName(lowOneLevelMarket.getMetricStruct().getLowEfficiencyUserName())
                .handlerLowEfficiencyUserAvgDuration(BigDecimal
                                                             .valueOf(lowOneLevelMarket.getMetricStruct().getLowEfficiencyUserAvgDuration())
                                                             .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .handlerLowEfficiencyUserMarketName(lowOneLevelMarket.getMarketName())
                .build();
    }

    private Long getAlarmNumberByEventType(Set<String> alarmEventTypeIds, AlarmStatisticsQueryParam queryParam) {
        queryParam.setAlarmEventTypeIds(alarmEventTypeIds);
        List<MarketMetricResult<AlarmBasicAnalysisStatisticsDTO>> allAlarmMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmBasicAnalysisStatisticsDTO>() {
                                     });
        return allAlarmMarketMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getAlarmNum())
                .reduce(Long::sum).orElse(0L);
    }

    private HeadMarketHandlerRateTopMetricDTO getHeadMarketHandlerRateTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        List<MarketHandlerRate> marketHandlerRates = getMarketHandlerRates(queryParam);
        List<MarketHandlerRate> topMarkets = marketHandlerRates.stream()
                .sorted(Comparator.comparing(MarketHandlerRate::getHandlerRate).reversed())
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
        List<MarketHandlerRate> lowMarkets = marketHandlerRates.stream().sorted(Comparator.comparing(MarketHandlerRate::getHandlerRate))
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
        return HeadMarketHandlerRateTopMetricDTO.builder()
                .topHandlerMarkets(topMarkets)
                .lowHandlerMarkets(lowMarkets)
                .build();
    }

    private List<MarketHandlerRate> getMarketHandlerRates(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<List<AlarmStatusStatisticsVO>>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_STATUS_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmStatusStatisticsVO>>() {
                                     });
        List<MarketHandlerRate> marketHandlerRates = new ArrayList<>();
        for (MarketMetricResult<List<AlarmStatusStatisticsVO>> result : allMarketMetrics) {
            if (CollectionUtils.isEmpty(result.getMetricStruct()) ||
                result.getMetricStruct().stream().noneMatch(status -> status.getAlarmStatus()
                        .equals(HANDLER_STATUS_CODE))) {
                continue;
            }
            long totalNum = result.getMetricStruct().stream().mapToLong(AlarmStatusStatisticsVO::getAlarmNum).sum();
            //已处理报警数量
            AlarmStatusStatisticsVO handler = result.getMetricStruct().stream().filter(status -> status.getAlarmStatus()
                            .equals(HANDLER_STATUS_CODE))
                    .findFirst().get();
            double rate = 0;
            if (totalNum > 0) {
                rate = BigDecimal.valueOf(handler.getAlarmNum())
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }
            marketHandlerRates.add(MarketHandlerRate.builder()
                                           .totalAlarmNum((int) totalNum)
                                           .totalAlarmHandlerNum(handler.getAlarmNum().intValue())
                                           .marketName(result.getMarketName())
                                           .handlerRate(rate)
                                           .build());
        }
        return marketHandlerRates;
    }

    private HeadUserHandlerRateTopMetricDTO getAlarmMarketUserHandlerRateTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<List<AlarmUserHandlerRateTopVO>>> topMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerRateTopVO>>() {
                                     });
        List<MarketMetricResult<List<AlarmUserHandlerRateTopVO>>> lastTopMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_RATE_LAST_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerRateTopVO>>() {
                                     });
        List<MarketUserHandlerRateDetails> topUserHandler = topMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   MarketUserHandlerRateDetails.builder()
                                                                           .userMarketName(t.getMarketName())
                                                                           .userHandlerAlarmNumber(user.getUserHandlerAlarmNumber()
                                                                                                           .intValue())
                                                                           .alarmHandlerUserName(user.getAlarmHandlerUserName())
                                                                           .userHandlerRate(user.getUserHandlerRate())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(MarketUserHandlerRateDetails::getUserHandlerRate).reversed())
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
        List<MarketUserHandlerRateDetails> lastTopUserHandler = lastTopMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   MarketUserHandlerRateDetails.builder()
                                                                           .userMarketName(t.getMarketName())
                                                                           .userHandlerAlarmNumber(user.getUserHandlerAlarmNumber()
                                                                                                           .intValue())
                                                                           .alarmHandlerUserName(user.getAlarmHandlerUserName())
                                                                           .userHandlerRate(user.getUserHandlerRate())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(MarketUserHandlerRateDetails::getUserHandlerRate))
                .limit(queryParam.getPageSize())
                .collect(Collectors.toList());
        return HeadUserHandlerRateTopMetricDTO.builder()
                .topHandlerUsers(topUserHandler)
                .lowHandlerUsers(lastTopUserHandler)
                .build();
    }

    private HeadMarketHandlerEfficiencyTopMetricDTO getHeadAlarmMarketHandlerEfficiencyTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<AlarmAvgHandlerEfficiencyVO>> allMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_EFFICIENCY_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmAvgHandlerEfficiencyVO>() {
                                     });
        List<MarketHandlerDuration> topMarketDetails = allMarketMetrics.stream()
                .filter(t -> !Objects.isNull(t.getMetricStruct()))
                .map(t ->
                             MarketHandlerDuration.builder()
                                     .marketName(t.getMarketName())
                                     .handlerDuration(BigDecimal.valueOf(t.getMetricStruct().getAvgHandlerDuration())
                                                              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build()
                    ).sorted(Comparator.comparing(MarketHandlerDuration::getHandlerDuration))
                .limit(3)
                .collect(Collectors.toList());
        List<MarketHandlerDuration> lowMarketDetails = allMarketMetrics.stream()
                .filter(t -> !Objects.isNull(t.getMetricStruct()))
                .map(t ->
                             MarketHandlerDuration.builder()
                                     .marketName(t.getMarketName())
                                     .handlerDuration(BigDecimal.valueOf(t.getMetricStruct().getAvgHandlerDuration())
                                                              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build()
                    ).sorted(Comparator.comparing(MarketHandlerDuration::getHandlerDuration)
                                     .reversed())
                .limit(3)
                .collect(Collectors.toList());
        return HeadMarketHandlerEfficiencyTopMetricDTO.builder()
                .topHandlerMarkets(topMarketDetails)
                .lowHandlerMarkets(lowMarketDetails)
                .build();
    }

    private HeadUserHandlerEfficiencyTopMetricDTO getHeadAlarmUserHandlerEfficiencyTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        List<MarketMetricResult<List<AlarmUserHandlerEfficiencyStatisticsVO>>> topMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerEfficiencyStatisticsVO>>() {
                                     });
        List<MarketMetricResult<List<AlarmUserHandlerEfficiencyStatisticsVO>>> lastTopMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_EFFICIENCY_LAST_TOP_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<List<AlarmUserHandlerEfficiencyStatisticsVO>>() {
                                     });
        List<MarketUserHandlerDuration> topUserDetails = topMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   MarketUserHandlerDuration.builder()
                                                                           .userMarketName(t.getMarketName())
                                                                           .alarmHandlerUserName(user.getAlarmHandlerName())
                                                                           .userAvgHandlerDuration(BigDecimal
                                                                                                           .valueOf(user.getAvgProcessSeconds())
                                                                                                           .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                                                                           .doubleValue())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(MarketUserHandlerDuration::getUserAvgHandlerDuration))
                .limit(3)
                .collect(Collectors.toList());
        List<MarketUserHandlerDuration> lowUserDetails = lastTopMarketMetrics.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user ->
                                                                   MarketUserHandlerDuration.builder()
                                                                           .userMarketName(t.getMarketName())
                                                                           .alarmHandlerUserName(user.getAlarmHandlerName())
                                                                           .userAvgHandlerDuration(BigDecimal
                                                                                                           .valueOf(user.getAvgProcessSeconds())
                                                                                                           .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                                                                           .doubleValue())
                                                                           .build()
                                                          ).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(MarketUserHandlerDuration::getUserAvgHandlerDuration).reversed())
                .limit(3)
                .collect(Collectors.toList());
        return HeadUserHandlerEfficiencyTopMetricDTO.builder()
                .topHandlerUsers(topUserDetails)
                .lowHandlerUsers(lowUserDetails)
                .build();
    }

    private static AlarmBimStatisticsQueryParam getAlarmBimStatisticsQueryParam(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam queryParam = new AlarmBimStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        if (Objects.nonNull(param.getPeriod())) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd HH:mm:ss") + "," +
                                 DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (!com.guhecloud.platform.base.util.CollectionUtils.isEmpty(param.getAppCodes())) {
            queryParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        }
        queryParam.setAlarmEventTypeIds(param.getEventTypes());
        return queryParam;
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 一级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmLevelCode(ONE_LEVEL_CODE);
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 二级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmLevelCode(TWO_LEVEL_CODE);
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 三级报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmLevelCode(THREE_LEVEL_CODE);
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 明火烟雾报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_FLAME_SMOG_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "明火烟雾报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getFlameSmogLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 违章占道报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ILOR_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违章占道报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getIlorLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(ILOR_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 交通拥堵报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_TC_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "交通拥堵报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTCLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(TC_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 水压报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_WP_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "水压报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getWPLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(WP_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 手报报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_MCP_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "手报报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getMCPLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(MCP_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * ups报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_UPS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "ups报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getUPSLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(UPS_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 温度报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温度报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getTemperatureLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(TEMPERATURE_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 违规占用消防通道报警处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_UPFL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违规占用消防通道报警处理效率指标")
    public HeadHandlerTimeoutRateMetricDTO getUpflAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(UPFL_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 温湿度报警处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_WSD_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温湿度报警报警处理率指标")
    public HeadHandlerTimeoutRateMetricDTO getWSDAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(WSD_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 电压报警处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_VAL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "电压报警报警处理率指标")
    public HeadHandlerTimeoutRateMetricDTO getValAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(VAL_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 漏电报警处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_LCA_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "漏电报警报警处理率指标")
    public HeadHandlerTimeoutRateMetricDTO getLcaAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(LCA_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public HeadHandlerRateMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, null));
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public HeadHandlerRateMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_FLAME_SMOG_ALARM_HANDLER_RATE_RANK_METRIC", description = "明火烟雾报警处理率指标")
    public HeadHandlerRateMetricDTO getFlameSmogAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_ILLEGAL_PARKING_ALARM_HANDLER_RATE_RANK_METRIC", description = "违章占道报警处理率指标")
    public HeadHandlerRateMetricDTO getIllegalParkingAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_TRAFFIC_JAM_ALARM_HANDLER_RATE_RANK_METRIC", description = "交通拥堵报警处理率指标")
    public HeadHandlerRateMetricDTO getTrafficJamAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_WATER_PRESSURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "水压报警处理率指标")
    public HeadHandlerRateMetricDTO getWaterPressureAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警
     */
    @MetricTopic(topic = "HEADQUARTERS_HAND_ALARM_HANDLER_RATE_RANK_METRIC", description = "手报报警处理率指标")
    public HeadHandlerRateMetricDTO getHandAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * UPS报警
     */
    @MetricTopic(topic = "HEADQUARTERS_UPS_ALARM_HANDLER_RATE_RANK_METRIC", description = "UPS报警处理率指标")
    public HeadHandlerRateMetricDTO getUpsAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温度报警
     */
    @MetricTopic(topic = "HEADQUARTERS_TEMPERATURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "温度报警处理率指标")
    public HeadHandlerRateMetricDTO getTemperatureAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道
     */
    @MetricTopic(topic = "HEADQUARTERS_UPFL_ALARM_HANDLER_RATE_RANK_METRIC", description = "违规占用消防通道报警处理率指标")
    public HeadHandlerRateMetricDTO getUpflAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 温湿度报警
     */
    @MetricTopic(topic = "HEADQUARTERS_WSD_ALARM_HANDLER_RATE_RANK_METRIC", description = "温湿度报警报警处理率指标")
    public HeadHandlerRateMetricDTO getWSDAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警
     */
    @MetricTopic(topic = "HEADQUARTERS_VAL_ALARM_HANDLER_RATE_RANK_METRIC", description = "电压报警报警处理率指标")
    public HeadHandlerRateMetricDTO getValAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警
     */
    @MetricTopic(topic = "HEADQUARTERS_LCA_ALARM_HANDLER_RATE_RANK_METRIC", description = "漏电报警报警处理率指标")
    public HeadHandlerRateMetricDTO getLcaAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    private AlarmStatisticsQueryParam convertQueryParam(MetricParam param, String level, Set<String> eventTypes) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setAlarmLevelCode(level);
        queryParam.setAlarmEventTypeIds(eventTypes);
        queryParam.setPeriod(param.getPeriod());
        return queryParam;
    }

    /**
     * 获取指定类型或者级别报警数据
     *
     * @param queryParam 查询参数
     *
     * @return AlarmUserHandlerRateTopMetricDTO
     */
    private HeadHandlerRateMetricDTO getAlarmUserHandlerRateTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        queryParam.setPageSize(1000);
        HeadUserHandlerRateTopMetricDTO alarmMarketUserHandlerRateTopMetricDTO = getAlarmMarketUserHandlerRateTopMetricDTO(queryParam);
        HeadMarketHandlerRateTopMetricDTO marketHandlerRateTopMetricDTO = getHeadMarketHandlerRateTopMetricDTO(queryParam);
        long totalAlarmNumber = marketHandlerRateTopMetricDTO.getTopHandlerMarkets().stream().mapToLong(MarketHandlerRate::getTotalAlarmNum)
                .sum();
        long totalAlarmHandlerNumber = marketHandlerRateTopMetricDTO.getTopHandlerMarkets().stream()
                .mapToLong(MarketHandlerRate::getTotalAlarmHandlerNum)
                .sum();
        return HeadHandlerRateMetricDTO.builder()
                .totalAlarmNumber(totalAlarmNumber)
                .handlerAlarmNumber(totalAlarmHandlerNumber)
                .handlerRate(0 == totalAlarmNumber ? 0 : BigDecimal.valueOf(totalAlarmHandlerNumber)
                        .divide(BigDecimal.valueOf(totalAlarmNumber), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue())
                .topHandlerMarkets(marketHandlerRateTopMetricDTO.getTopHandlerMarkets().stream().limit(5).collect(Collectors.toList()))
                .lowHandlerMarkets(marketHandlerRateTopMetricDTO.getLowHandlerMarkets().stream().limit(5).collect(Collectors.toList()))
                .topHandlerUsers(alarmMarketUserHandlerRateTopMetricDTO.getTopHandlerUsers().stream().limit(5).collect(Collectors.toList()))
                .lowHandlerUsers(alarmMarketUserHandlerRateTopMetricDTO.getLowHandlerUsers().stream().limit(5).collect(Collectors.toList()))
                .build();
    }

    /**
     * 获取处理超时占比数据
     *
     * @param param
     *
     * @return
     */
    private HeadHandlerTimeoutRateMetricDTO getCommonAlarmHandlerTimeoutEfficiencyMetric(AlarmBimStatisticsQueryParam param) {
        //总超时占比数据
        List<MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO>> marketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<AlarmHandlerTimeoutRateStatisticsVO>() {
                                     });
        //市场用户超时占比数据
        List<MarketMetricResult<List<AlarmUserHandlerTimeoutRateStatisticsVO>>> alarmUserHandlerTimeoutRate = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_USER_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, param, param,
                                     new TypeReference<List<AlarmUserHandlerTimeoutRateStatisticsVO>>() {
                                     });
        //市场处理列表
        List<MarketTimeoutHandlerRate> marketTimeoutHandlerRates = new ArrayList<>();
        //获取市场总处理数
        int totalHandlerNum = 0;
        int totalOnTimeHandlerNum = 0;
        int totalTimeoutHandlerNum = 0;
        //获取市场按时处理的报警数量
        for (MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO> marketMetricResult : marketMetrics) {
            int marketOnTimeHandlerNum = 0;
            AlarmHandlerTimeoutRateStatisticsVO metricStruct = marketMetricResult.getMetricStruct();
            if (Objects.nonNull(metricStruct.getTotalHandlerTimeoutNum()) && Objects.nonNull(metricStruct.getTotalHandlerNum())) {
                marketOnTimeHandlerNum = marketMetricResult.getMetricStruct().getTotalHandlerNum() -
                                         marketMetricResult.getMetricStruct().getTotalHandlerTimeoutNum();
            }
            totalHandlerNum += metricStruct.getTotalHandlerNum();
            totalOnTimeHandlerNum += marketOnTimeHandlerNum;
            totalTimeoutHandlerNum += metricStruct.getTotalHandlerTimeoutNum();
            marketTimeoutHandlerRates.add(MarketTimeoutHandlerRate.builder()
                                                  .marketName(marketMetricResult.getMarketName())
                                                  .totalHandlerNum(metricStruct.getTotalHandlerNum())
                                                  .onTimeHandlerNum(marketOnTimeHandlerNum)
                                                  .totalHandlerTimeoutNum(metricStruct.getTotalHandlerTimeoutNum())
                                                  .onTimeHandlerRate(
                                                          0 == marketOnTimeHandlerNum ? 0 : BigDecimal.valueOf(marketOnTimeHandlerNum)
                                                                  .divide(BigDecimal.valueOf(metricStruct.getTotalHandlerNum()), 2, RoundingMode.HALF_UP)
                                                                  .multiply(BigDecimal.valueOf(100))
                                                                  .doubleValue())
                                                  .build());
        }
        //用户超时占比数据
        List<MarketUserTimeoutHandlerRate> userTimeoutHandlerRates = alarmUserHandlerTimeoutRate.stream()
                .filter(t -> CollectionUtils.isNotEmpty(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().stream().map(user -> {
                    int usertOnTimeHandlerNum = 0;
                    if (Objects.nonNull(user.getTotalHandlerTimeoutNum()) && Objects.nonNull(user.getTotalHandlerNum())) {
                        usertOnTimeHandlerNum = user.getTotalHandlerNum() -
                                                user.getTotalHandlerTimeoutNum();
                    }
                    return MarketUserTimeoutHandlerRate.builder()
                            .marketName(t.getMarketName())
                            .userName(user.getUsername())
                            .totalHandlerNum(user.getTotalHandlerNum())
                            .onTimeHandlerNum(user.getTotalHandlerNum() -
                                              user.getTotalHandlerTimeoutNum())
                            .totalHandlerTimeoutNum(user.getTotalHandlerTimeoutNum())
                            .onTimeHandlerRate(0 == usertOnTimeHandlerNum ? 0 : BigDecimal.valueOf(usertOnTimeHandlerNum)
                                    .divide(BigDecimal.valueOf(user.getTotalHandlerNum()), 2, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100))
                                    .doubleValue())
                            .build();
                }).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return HeadHandlerTimeoutRateMetricDTO.builder()
                .totalHandlerNum(totalHandlerNum)
                .onTimeHandlerNum(totalOnTimeHandlerNum)
                .totalHandlerTimeoutNum(totalTimeoutHandlerNum)
                .onTimeHandlerRate(0 == totalOnTimeHandlerNum ? 0 : BigDecimal.valueOf(totalOnTimeHandlerNum)
                        .divide(BigDecimal.valueOf(totalHandlerNum), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue())
                .topOnTimeHandlerMarkets(marketTimeoutHandlerRates.stream()
                                                 .sorted(Comparator.comparing(MarketTimeoutHandlerRate::getOnTimeHandlerRate).reversed())
                                                 .limit(5).collect(Collectors.toList()))
                .lowOnTimeHandlerMarkets(marketTimeoutHandlerRates.stream()
                                                 .sorted(Comparator.comparing(MarketTimeoutHandlerRate::getOnTimeHandlerRate))
                                                 .limit(5).collect(Collectors.toList()))
                .topOnTimeHandlerUsers(userTimeoutHandlerRates.stream()
                                               .sorted(Comparator.comparing(MarketUserTimeoutHandlerRate::getOnTimeHandlerRate).reversed())
                                               .limit(5).collect(Collectors.toList()))
                .lowOnTimeHandlerUsers(userTimeoutHandlerRates.stream()
                                               .sorted(Comparator.comparing(MarketUserTimeoutHandlerRate::getOnTimeHandlerRate))
                                               .limit(5).collect(Collectors.toList()))
                .build();
    }

    private AlarmStatisticsQueryParam convertAlarmMetricParam(McpAlarmQueryParam param) {
        AlarmStatisticsQueryParam alarmMetricParam = new AlarmStatisticsQueryParam();
        if (Objects.nonNull(param.getBeginTime()) && Objects.nonNull(param.getEndTime())) {
            alarmMetricParam.setPeriod(new Period(param.getBeginTime(), param.getEndTime()));
        }
        alarmMetricParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        alarmMetricParam.setAlarmEventTypeIds(param.getEventTypes());
        alarmMetricParam.setAlarmLevelCode(param.getAlarmLevel());
        return alarmMetricParam;
    }

    public List<MarketAlarmNum> getAlarmNumberRank(McpAlarmQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertAlarmMetricParam(param);
        List<MarketMetricResult<AlarmBasicAnalysisStatisticsDTO>> allAlarmMarketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_NUMBER_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmBasicAnalysisStatisticsDTO>() {
                                     });
        return allAlarmMarketMetrics.stream().map(m -> MarketAlarmNum.builder()
                        .marketName(m.getMarketName())
                        .alarmNumber(m.getMetricStruct().getAlarmNum().intValue())
                        .build())
                .sorted(Comparator.comparing(MarketAlarmNum::getAlarmNumber).reversed())
                .collect(Collectors.toList());
    }

    public List<MarketHandlerRate> getAlarmHandlerRateMarketRank(McpAlarmQueryParam param) {
        AlarmStatisticsQueryParam queryParam = this.convertAlarmMetricParam(param);
        return getMarketHandlerRates(queryParam)
                .stream().sorted(Comparator.comparing(MarketHandlerRate::getHandlerRate).reversed()).collect(Collectors.toList());
    }

    public List<MarketTimeoutHandlerRate> getAlarmHandlerEfficiencyMarketRank(McpAlarmQueryParam param) {
        AlarmBimStatisticsQueryParam queryParam = new AlarmBimStatisticsQueryParam();
        if (Objects.nonNull(param.getEndTime()) && Objects.nonNull(param.getBeginTime())) {
            queryParam.setPeriod(DateUtils.format(param.getBeginTime(), "yyyy-MM-dd HH:mm:ss") + "," +
                                 DateUtils.format(param.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (!CollectionUtils.isEmpty(param.getAppCodes())) {
            queryParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        }
        queryParam.setAlarmEventTypeIds(param.getEventTypes());
        queryParam.setAlarmLevelCode(param.getAlarmLevel());

        List<MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO>> marketMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL, HttpMethod.POST, queryParam, queryParam,
                                     new TypeReference<AlarmHandlerTimeoutRateStatisticsVO>() {
                                     });
        //市场处理列表
        List<MarketTimeoutHandlerRate> marketTimeoutHandlerRates = new ArrayList<>();
        //获取市场按时处理的报警数量
        for (MarketMetricResult<AlarmHandlerTimeoutRateStatisticsVO> marketMetricResult : marketMetrics) {
            int marketOnTimeHandlerNum = 0;
            AlarmHandlerTimeoutRateStatisticsVO metricStruct = marketMetricResult.getMetricStruct();
            if (Objects.nonNull(metricStruct.getTotalHandlerTimeoutNum()) && Objects.nonNull(metricStruct.getTotalHandlerNum())) {
                marketOnTimeHandlerNum = marketMetricResult.getMetricStruct().getTotalHandlerNum() -
                                         marketMetricResult.getMetricStruct().getTotalHandlerTimeoutNum();
            }
            marketTimeoutHandlerRates.add(MarketTimeoutHandlerRate.builder()
                                                  .marketName(marketMetricResult.getMarketName())
                                                  .totalHandlerNum(metricStruct.getTotalHandlerNum())
                                                  .onTimeHandlerNum(marketOnTimeHandlerNum)
                                                  .totalHandlerTimeoutNum(metricStruct.getTotalHandlerTimeoutNum())
                                                  .onTimeHandlerRate(
                                                          0 == marketOnTimeHandlerNum ? 0 : BigDecimal.valueOf(marketOnTimeHandlerNum)
                                                                  .divide(BigDecimal.valueOf(metricStruct.getTotalHandlerNum()), 2, RoundingMode.HALF_UP)
                                                                  .multiply(BigDecimal.valueOf(100))
                                                                  .doubleValue())
                                                  .build());
        }
        return marketTimeoutHandlerRates.stream()
                .sorted(Comparator.comparing(MarketTimeoutHandlerRate::getOnTimeHandlerRate).reversed()).collect(Collectors.toList());
    }

    public List<MarketHandlerRateLevel> getAlarmHandlerRateTopMarketRank(McpAlarmQueryParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        queryParam.setPeriod(defautlPeriod(param.getBeginTime(), param.getEndTime()));
        //报警级别数量和处理率
        List<MarketMetricResult<AlarmLevelTopStatisticsVO>> allMarketLevelMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(ALARM_LEVEL_STATISTICS_REQUEST_URL, HttpMethod.GET, null, queryParam,
                                     new TypeReference<AlarmLevelTopStatisticsVO>() {
                                     });
        return allMarketLevelMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t ->
                             MarketHandlerRateLevel.builder()
                                     .marketName(t.getMarketName())
                                     .maxAlarmLevel(t.getMetricStruct().getMaxAlarmLevel())
                                     .maxAlarmCount(t.getMetricStruct().getMaxAlarmCount())
                                     .maxHandlerLevel(t.getMetricStruct().getMaxHandlerLevel())
                                     .maxHandlerRate(t.getMetricStruct().getMaxHandlerRate())
                                     .maxEfficiencyLevel(t.getMetricStruct().getMaxEfficiencyLevel())
                                     .maxEfficiencyRate(t.getMetricStruct().getMaxEfficiencyRate())
                                     .build()
                    )
                .collect(Collectors.toList());
    }
}
