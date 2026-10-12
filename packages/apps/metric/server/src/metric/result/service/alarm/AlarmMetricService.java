package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm;

import com.guhecloud.cube.base.alarm.client.AlarmBimStatisticsClient;
import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.client.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmBimHandlerResultStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmBimStatisticsHandlerVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.client.vo.AlarmUserHandlerTimeoutRateStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.AlarmMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmAreaNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmEventTypeNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmFlameSmogHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmFlameSmogHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmFlameSmogNumMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmHandlerTimeoutRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmImpracticalRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelHandlerRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmLevelNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmOneLevelUserHandlerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmUserHandlerEfficiencyTopMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto.AlarmUserHandlerRateTopMetricDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants.AlarmMetricConstant.FlAME_SMOG_EVENT_CODES;
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

@Slf4j
@Service
public class AlarmMetricService {

    private final AlarmStatisticsClient alarmStatisticsClient;

    private final AlarmBimStatisticsClient alarmBimStatisticsClient;

    public AlarmMetricService(AlarmStatisticsClient alarmStatisticsClient, AlarmBimStatisticsClient alarmBimStatisticsClient) {
        this.alarmStatisticsClient = alarmStatisticsClient;
        this.alarmBimStatisticsClient = alarmBimStatisticsClient;
    }

    /**
     * 报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_NUMBER", description = "报警总数指标")
    public AlarmNumberMetricDTO getAlarmNumberMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setType("THIS_YEAR");
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisRingStatistics = this.alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        return AlarmNumberMetricDTO.builder()
                .alarmNumber(alarmBasicAnalysisRingStatistics.getAlarmNum().intValue())
                .build();
    }

    /**
     * 按级别统计报警总数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_LEVEL_NUMBER", description = "报警级别数量指标")
    public List<AlarmLevelNumberMetricDTO> getAlarmLevelStatistics(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisRingStatistics = this.alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        List<AlarmLevelStatisticsVO> alarmLevelStatistics = this.alarmStatisticsClient.getAlarmLevelStatistics(queryParam);
        return alarmLevelStatistics.stream().map(alarmLevelStatisticsVO -> {
            AlarmLevelNumberMetricDTO metricDTO = AlarmLevelNumberMetricDTO.builder()
                    .code(alarmLevelStatisticsVO.getCode().toUpperCase())
                    .name(alarmLevelStatisticsVO.getName())
                    .alarmNum(alarmLevelStatisticsVO.getAlarmNum())
                    .build();
            double ratio = 0;
            if (Objects.isNull(alarmBasicAnalysisRingStatistics) || 0 == alarmBasicAnalysisRingStatistics.getAlarmNum().intValue()) {
                ratio = 0.0;
            } else {
                ratio = BigDecimal.valueOf(alarmLevelStatisticsVO.getAlarmNum())
                        .divide(BigDecimal.valueOf(alarmBasicAnalysisRingStatistics.getAlarmNum()), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }
            metricDTO.setRatio(ratio);
            return metricDTO;
        }).collect(Collectors.toList());
    }

    private static AlarmStatisticsQueryParam getAlarmStatisticsQueryParam(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = new AlarmStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPageSize(param.getSize());
        if (!CollectionUtils.isEmpty(param.getAppCodes())) {
            queryParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        }
        queryParam.setAlarmEventTypeIds(param.getEventTypes());
        return queryParam;
    }

    /**
     * 报警总处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_HANDLER_RATE", description = "报警总处理率指标")
    public AlarmHandlerRateMetricDTO getAlarmHandlerRateMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisRingStatistics = this.alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        if (0 == alarmBasicAnalysisRingStatistics.getAlarmNum()) {
            return new AlarmHandlerRateMetricDTO(0, 0.0);
        }
        List<AlarmStatusStatisticsVO> alarmStatusStatistics = this.alarmStatisticsClient.getAlarmStatusStatistics(queryParam);
        Optional<AlarmStatusStatisticsVO> handlerOpt = alarmStatusStatistics.stream().filter(status -> status.getAlarmStatus()
                        .equals("ALARM_HANDLER"))
                .findFirst();
        if (!handlerOpt.isPresent() || 0 == handlerOpt.get().getAlarmNum()) {
            return new AlarmHandlerRateMetricDTO(0, 0.0);
        }
        return AlarmHandlerRateMetricDTO.builder()
                .alarmHandlerNumber(handlerOpt.get().getAlarmNum().intValue())
                .alarmHandlerRate(BigDecimal.valueOf(handlerOpt.get().getAlarmNum())
                                          .divide(BigDecimal.valueOf(alarmBasicAnalysisRingStatistics.getAlarmNum()), 2, RoundingMode.HALF_UP)
                                          .multiply(BigDecimal.valueOf(100))
                                          .doubleValue())
                .build();
    }

    /**
     * 报警级别处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_LEVEL_HANDLER_RATE", description = "报警级别处理率指标")
    public List<AlarmLevelHandlerRateMetricDTO> getAlarmLevelHandlerRateMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        List<AlarmLevelStatisticsVO> alarmLevelHandlerRateStatistics = this.alarmStatisticsClient.getAlarmLevelHandlerRateStatistics(queryParam);
        return alarmLevelHandlerRateStatistics.stream().map(alarmLevelStatisticsVO ->
                                                                    AlarmLevelHandlerRateMetricDTO.builder()
                                                                            .code(alarmLevelStatisticsVO.getCode().toUpperCase())
                                                                            .name(alarmLevelStatisticsVO.getName())
                                                                            .alarmNum(alarmLevelStatisticsVO.getAlarmNum())
                                                                            .handlerNum(alarmLevelStatisticsVO.getHandlerNum())
                                                                            .handlerRate(alarmLevelStatisticsVO.getHandlerRate())
                                                                            .build()
                                                           ).collect(Collectors.toList());
    }

    /**
     * 报警总处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_HANDLER_EFFICIENCY", description = "报警总处理效率指标")
    public AlarmHandlerEfficiencyMetricDTO getAlarmHandlerEfficiencyMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        AlarmAvgHandlerEfficiencyVO alarmAvgHandlerEfficiency = this.alarmStatisticsClient.getAlarmAvgHandlerEfficiency(queryParam);
        return AlarmHandlerEfficiencyMetricDTO.builder()
                .alarmAvgHandlerDuration(BigDecimal.valueOf(alarmAvgHandlerEfficiency.getAvgHandlerDuration())
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
    @MetricTopic(topic = "ALARM_LEVEL_HANDLER_EFFICIENCY", description = "报警分级处理效率指标")
    public List<AlarmLevelHandlerEfficiencyMetricDTO> getAlarmLevelHandlerEfficiencyMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        List<AlarmLevelHandlerEfficiencyStatisticsVO> alarmLevelHandlerEfficiency = this.alarmStatisticsClient.getAlarmLevelHandlerEfficiency(queryParam);
        return alarmLevelHandlerEfficiency.stream().map(alarmLevelEfficiency ->
                                                                AlarmLevelHandlerEfficiencyMetricDTO.builder()
                                                                        .code(alarmLevelEfficiency.getCode().toUpperCase())
                                                                        .name(alarmLevelEfficiency.getName())
                                                                        .handlerNum(alarmLevelEfficiency.getHandlerNum())
                                                                        .durationTotal(alarmLevelEfficiency.getDurationTotal())
                                                                        .avgHandlerDuration(
                                                                                alarmLevelEfficiency.getAvgHandlerDuration() / 60)
                                                                        .build()
                                                       ).collect(Collectors.toList());
    }

    /**
     * 明火烟雾报警数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_FLAME_SMOG_NUMBER", description = "明火烟雾报警数量指标")
    public AlarmFlameSmogNumMetricDTO getFlameSmogNumMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisRingStatistics = alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        if (Objects.isNull(alarmBasicAnalysisRingStatistics) || 0 == alarmBasicAnalysisRingStatistics.getAlarmNum()) {
            return new AlarmFlameSmogNumMetricDTO(0, 0.0);
        }
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        AlarmBasicAnalysisStatisticsDTO alarmEventBasicAnalysisRingStatistics = alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        return new AlarmFlameSmogNumMetricDTO(alarmEventBasicAnalysisRingStatistics.getAlarmNum().intValue(),
                                              BigDecimal.valueOf(alarmEventBasicAnalysisRingStatistics.getAlarmNum())
                                                      .divide(BigDecimal.valueOf(alarmBasicAnalysisRingStatistics.getAlarmNum()), 2, RoundingMode.HALF_UP)
                                                      .multiply(BigDecimal.valueOf(100))
                                                      .doubleValue());
    }

    /**
     * 明火烟雾报警处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_FLAME_SMOG_HANDLER_RATE", description = "明火烟雾报警处理率指标")
    public AlarmFlameSmogHandlerRateMetricDTO getFlameSmogHandlerRateMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisRingStatistics = this.alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(queryParam);
        if (0 == alarmBasicAnalysisRingStatistics.getAlarmNum()) {
            return new AlarmFlameSmogHandlerRateMetricDTO(0, 0.0);
        }
        List<AlarmStatusStatisticsVO> alarmStatusStatistics = this.alarmStatisticsClient.getAlarmStatusStatistics(queryParam);
        Optional<AlarmStatusStatisticsVO> handlerOpt = alarmStatusStatistics.stream().filter(status -> status.getAlarmStatus()
                        .equals("ALARM_HANDLER"))
                .findFirst();
        if (!handlerOpt.isPresent() || 0 == handlerOpt.get().getAlarmNum()) {
            return new AlarmFlameSmogHandlerRateMetricDTO(0, 0.0);
        }
        return AlarmFlameSmogHandlerRateMetricDTO.builder()
                .flameSmogAlarmHandlerNumber(handlerOpt.get().getAlarmNum().intValue())
                .flameSmogAlarmHandlerRate(BigDecimal.valueOf(handlerOpt.get().getAlarmNum())
                                                   .divide(BigDecimal.valueOf(alarmBasicAnalysisRingStatistics.getAlarmNum()), 2, RoundingMode.HALF_UP)
                                                   .multiply(BigDecimal.valueOf(100))
                                                   .doubleValue())
                .build();
    }

    /**
     * 明火烟雾报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_FLAME_SMOG_HANDLER_EFFICIENCY", description = "明火烟雾报警处理效率指标")
    public AlarmFlameSmogHandlerEfficiencyMetricDTO getFlameSmogHandlerEfficiencyMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setAlarmEventTypeIds(FlAME_SMOG_EVENT_CODES);
        AlarmAvgHandlerEfficiencyVO alarmAvgHandlerEfficiency = this.alarmStatisticsClient.getAlarmAvgHandlerEfficiency(queryParam);
        return AlarmFlameSmogHandlerEfficiencyMetricDTO.builder()
                .flameSmogAlarmAvgHandlerDuration(BigDecimal.valueOf(alarmAvgHandlerEfficiency.getAvgHandlerDuration())
                                                          .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .maxHandlerDuration(BigDecimal.valueOf(Objects.isNull(alarmAvgHandlerEfficiency.getMaxHandlerDuration()) ? 0
                                                                                                                         : alarmAvgHandlerEfficiency.getMaxHandlerDuration())
                                            .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .minHandlerDuration(BigDecimal.valueOf(Objects.isNull(alarmAvgHandlerEfficiency.getMinHandlerDuration()) ? 0
                                                                                                                         : alarmAvgHandlerEfficiency.getMinHandlerDuration())
                                            .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    /**
     * 报警类型数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_EVENT_TYPE_NUM", description = "报警类型处理效率指标")
    public List<AlarmEventTypeNumberMetricDTO> getAlarmEventTypeNumberMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam queryParam = getAlarmBimStatisticsQueryParam(param);
        if (Objects.nonNull(param.getPeriod())) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd HH:mm:ss") + "," +
                                 DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd HH:mm:ss"));
        }
        List<AlarmBimStatisticsHandlerVO> eventTypeHandlerListStatistics = alarmBimStatisticsClient.getEventTypeHandlerListStatistics(queryParam);
        return eventTypeHandlerListStatistics.stream().map(alarmBimStatisticsHandlerVO ->
                                                                   AlarmEventTypeNumberMetricDTO.builder()
                                                                           .alarmTypeCode(alarmBimStatisticsHandlerVO.getId())
                                                                           .alarmTypeName(alarmBimStatisticsHandlerVO.getName())
                                                                           .alarmTypeNumber(alarmBimStatisticsHandlerVO.getTotalNum())
                                                                           .build()
                                                          ).collect(Collectors.toList());
    }

    /**
     * 报警类型处理处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_EVENT_TYPE_HANDLER_RATE", description = "报警类型处理处理率指标")
    public List<AlarmEventTypeHandlerRateMetricDTO> getAlarmEventTypeHandlerRateMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam queryParam = getAlarmBimStatisticsQueryParam(param);
        List<AlarmBimStatisticsHandlerVO> eventTypeHandlerListStatistics = alarmBimStatisticsClient.getEventTypeHandlerListStatistics(queryParam);
        return eventTypeHandlerListStatistics.stream().map(alarmBimStatisticsHandlerVO -> {
                                                               double rate = 0;
                                                               if (0 < alarmBimStatisticsHandlerVO.getTotalNum()) {
                                                                   rate = BigDecimal.valueOf(alarmBimStatisticsHandlerVO.getHandlerNum())
                                                                           .divide(BigDecimal.valueOf(alarmBimStatisticsHandlerVO.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                                           .multiply(BigDecimal.valueOf(100))
                                                                           .doubleValue();
                                                               }
                                                               return AlarmEventTypeHandlerRateMetricDTO.builder()
                                                                       .alarmTypeCode(alarmBimStatisticsHandlerVO.getId())
                                                                       .alarmTypeName(alarmBimStatisticsHandlerVO.getName())
                                                                       .alarmTypeNumber(alarmBimStatisticsHandlerVO.getTotalNum())
                                                                       .alarmTypeHandlerNumber(alarmBimStatisticsHandlerVO.getHandlerNum())
                                                                       .alarmTypeHandlerRate(rate)
                                                                       .build();
                                                           }
                                                          ).collect(Collectors.toList());
    }

    private static AlarmBimStatisticsQueryParam getAlarmBimStatisticsQueryParam(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam queryParam = new AlarmBimStatisticsQueryParam();
        queryParam.setApplicationCode(param.getAppCode());
        if (Objects.nonNull(param.getPeriod())) {
            queryParam.setPeriod(DateUtils.format(param.getPeriod().getBegin(), "yyyy-MM-dd HH:mm:ss") + "," +
                                 DateUtils.format(param.getPeriod().getEnd(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (!CollectionUtils.isEmpty(param.getAppCodes())) {
            queryParam.setApplicationCodeList(new ArrayList<>(param.getAppCodes()));
        }
        queryParam.setAlarmEventTypeIds(param.getEventTypes());
        return queryParam;
    }

    /**
     * 报警误报率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_IMPRACTICAL_RATE", description = "报警误报率指标")
    public AlarmImpracticalRateMetricDTO getAlarmImpracticalRateMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam queryParam = getAlarmBimStatisticsQueryParam(param);
        queryParam.setType("THIS_YEAR");
        AlarmBimHandlerResultStatisticsVO alarmResultStatistics = alarmBimStatisticsClient.getAlarmResultStatistics(queryParam);
        if (Objects.isNull(alarmResultStatistics) || 0 == alarmResultStatistics.getTotalNum()) {
            return new AlarmImpracticalRateMetricDTO(0L, 0L, 0.0);
        }
        return new AlarmImpracticalRateMetricDTO(alarmResultStatistics.getActualNum(),
                                                 alarmResultStatistics.getImpracticalNum(),
                                                 BigDecimal.valueOf(alarmResultStatistics.getImpracticalNum())
                                                         .divide(BigDecimal.valueOf(alarmResultStatistics.getTotalNum()), 2, RoundingMode.HALF_UP)
                                                         .multiply(BigDecimal.valueOf(100))
                                                         .doubleValue());
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_USER_HANDLER_RATE_TOP", description = "处理率排名指标")
    public AlarmUserHandlerRateTopMetricDTO getUserHandlerRateTopMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setPageSize(3);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateTopStatistics(queryParam);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateLastTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateLastTopStatistics(queryParam);
        List<AlarmUserHandlerRateTopMetricDTO.UserDetails> topUserDetails = alarmUserHandlerRateTopStatistics.stream().map(top ->
                                                                                                                                   AlarmUserHandlerRateTopMetricDTO.UserDetails
                                                                                                                                           .builder()
                                                                                                                                           .userHandlerAlarmNumber(top
                                                                                                                                                                           .getUserHandlerAlarmNumber()
                                                                                                                                                                           .intValue())
                                                                                                                                           .alarmHandlerUserName(top.getAlarmHandlerUserName())
                                                                                                                                           .userHandlerRate(top.getUserHandlerRate())
                                                                                                                                           .build()
                                                                                                                          )
                .collect(Collectors.toList());
        List<AlarmUserHandlerRateTopMetricDTO.UserDetails> lowUserDetails = alarmUserHandlerRateLastTopStatistics.stream().map(top ->
                                                                                                                                       AlarmUserHandlerRateTopMetricDTO.UserDetails
                                                                                                                                               .builder()
                                                                                                                                               .userHandlerAlarmNumber(top
                                                                                                                                                                               .getUserHandlerAlarmNumber()
                                                                                                                                                                               .intValue())
                                                                                                                                               .alarmHandlerUserName(top.getAlarmHandlerUserName())
                                                                                                                                               .userHandlerRate(top.getUserHandlerRate())
                                                                                                                                               .build()
                                                                                                                              )
                .collect(Collectors.toList());
        return AlarmUserHandlerRateTopMetricDTO.builder()
                .topHandlerUsers(topUserDetails)
                .lowHandlerUsers(lowUserDetails)
                .build();
    }

    /**
     * 处理效率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_USER_HANDLER_EFFICIENCY_TOP_METRIC", description = "处理效率排名指标")
    public AlarmUserHandlerEfficiencyTopMetricDTO getUserHandlerEfficiencyTopMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setPageSize(3);
        List<AlarmUserHandlerEfficiencyStatisticsVO> alarmUserHandlerEfficiencyTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerEfficiencyTopStatistics(queryParam);
        List<AlarmUserHandlerEfficiencyStatisticsVO> alarmUserHandlerEfficiencyLastTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerEfficiencyLastTopStatistics(queryParam);
        List<AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails> topUserDetails = alarmUserHandlerEfficiencyTopStatistics.stream()
                .map(top ->
                             AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails.builder()
                                     .alarmHandlerUserName(top.getAlarmHandlerName())
                                     .userAvgHandlerDuration(BigDecimal.valueOf(top.getAvgProcessSeconds())
                                                                     .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build()
                    ).collect(Collectors.toList());
        List<AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails> lowUserDetails = alarmUserHandlerEfficiencyLastTopStatistics.stream()
                .map(top ->
                             AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails.builder()
                                     .alarmHandlerUserName(top.getAlarmHandlerName())
                                     .userAvgHandlerDuration(BigDecimal.valueOf(top.getAvgProcessSeconds())
                                                                     .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build()
                    ).collect(Collectors.toList());
        return AlarmUserHandlerEfficiencyTopMetricDTO.builder()
                .topHandlerUsers(topUserDetails)
                .lowHandlerUsers(lowUserDetails)
                .build();
    }

    /**
     * 一级报警处理效率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ONE_LEVEL_ALARM_HANDLER_EFFICIENCY_TOP_METRIC", description = "一级报警处理效率指标排名")
    public AlarmOneLevelUserHandlerEfficiencyMetricDTO getOneLevelUserHandlerEfficiencyMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        List<AlarmLevelHandlerEfficiencyStatisticsVO> alarmLevelHandlerEfficiencyStatistics = this.alarmStatisticsClient.getAlarmLevelHandlerEfficiencyStatistics(queryParam);
        if (CollectionUtils.isEmpty(alarmLevelHandlerEfficiencyStatistics)) {
            return new AlarmOneLevelUserHandlerEfficiencyMetricDTO();
        }
        Optional<AlarmLevelHandlerEfficiencyStatisticsVO> oneLevelOpt = alarmLevelHandlerEfficiencyStatistics.stream()
                .filter(level -> ONE_LEVEL_CODE.equals(level.getCode()))
                .findFirst();
        if (!oneLevelOpt.isPresent()) {
            return new AlarmOneLevelUserHandlerEfficiencyMetricDTO();
        }
        AlarmLevelHandlerEfficiencyStatisticsVO oneLevel = oneLevelOpt.get();
        return AlarmOneLevelUserHandlerEfficiencyMetricDTO.builder()
                .code(oneLevel.getCode())
                .name(oneLevel.getName())
                .handlerHighEfficiencyUserName(oneLevel.getHighEfficiencyUserName())
                .handlerHighEfficiencyUserAvgDuration(BigDecimal.valueOf(oneLevel.getHighEfficiencyUserAvgDuration())
                                                              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .handlerLowEfficiencyUserName(oneLevel.getLowEfficiencyUserName())
                .handlerLowEfficiencyUserAvgDuration(BigDecimal.valueOf(oneLevel.getLowEfficiencyUserAvgDuration())
                                                             .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    /**
     * 区域报警数指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "AREA_ALARM_NUMBER_METRIC", description = "区域报警数指标")
    public List<AlarmAreaNumberMetricDTO> getAreaAlarmNumberMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setAlarmHandlerResult(AlarmHandlerResultEnum.ALARM_ACTUAL.name());
        List<AlarmCurrencyStatisticsVO> alarmAreaStatistics = this.alarmStatisticsClient.getAlarmAreaStatistics(queryParam);
        return alarmAreaStatistics.stream()
                .map(area ->
                             AlarmAreaNumberMetricDTO.builder()
                                     .areaName(area.getName())
                                     .areaAlarmNumber(area.getTotalNum())
                                     .build()
                    )
                .collect(Collectors.toList());
    }

    /**
     * 用户报警处理数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_USER_HANDLER_RATE_METRIC", description = "用户报警处理数量指标")
    public List<AlarmUserHandlerRateTopMetricDTO.UserDetails> getUserHandlerRateMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setPageSize(10000);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateTopStatistics(queryParam);
        return alarmUserHandlerRateTopStatistics.stream()
                .map(top ->
                             AlarmUserHandlerRateTopMetricDTO.UserDetails
                                     .builder()
                                     .userHandlerAlarmNumber(top
                                                                     .getUserHandlerAlarmNumber()
                                                                     .intValue())
                                     .alarmHandlerUserName(top.getAlarmHandlerUserName())
                                     .userHandlerRate(top.getUserHandlerRate())
                                     .build()
                    )
                .collect(Collectors.toList());
    }

    /**
     * 用户报警处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_USER_HANDLER_EFFICIENCY_METRIC", description = "用户报警处理效率指标")
    public List<AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails> getUserHandlerEfficiencyMetric(AlarmMetricParam param) {
        AlarmStatisticsQueryParam queryParam = getAlarmStatisticsQueryParam(param);
        queryParam.setPageSize(10000);
        List<AlarmUserHandlerEfficiencyStatisticsVO> alarmUserHandlerEfficiencyTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerEfficiencyTopStatistics(queryParam);
        return alarmUserHandlerEfficiencyTopStatistics.stream()
                .map(top ->
                             AlarmUserHandlerEfficiencyTopMetricDTO.UserDetails.builder()
                                     .alarmHandlerUserName(top.getAlarmHandlerName())
                                     .userAvgHandlerDuration(BigDecimal.valueOf(top.getAvgProcessSeconds())
                                                                     .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue())
                                     .build()
                    ).collect(Collectors.toList());
    }

    /**
     * 处理效率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "ONE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getOneLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "TWO_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTwoLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "THREE_LEVEL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "一级报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getThreeLevelAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "FLAME_SMOG_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "明火烟雾报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getFlameSmogAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "ILOR_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违章占道报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getIlorAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "TC_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "交通拥堵报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTCAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "WP_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "水压报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getWPAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "MCP_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "手报报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getMCPAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "UPS_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "ups报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getUPSAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
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
    @MetricTopic(topic = "TEMPERATURE_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温度报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getTemperatureAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(TEMPERATURE_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 违规占用消防通道报警处理效率指标
     */
    @MetricTopic(topic = "UPFL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "违规占用消防通道报警处理效率指标")
    public AlarmHandlerTimeoutRateMetricDTO getUpflAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(UPFL_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 温湿度报警处理效率指标
     */
    @MetricTopic(topic = "WSD_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "温湿度报警报警处理率指标")
    public AlarmHandlerTimeoutRateMetricDTO getWSDAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(WSD_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 电压报警处理效率指标
     */
    @MetricTopic(topic = "VAL_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "电压报警报警处理率指标")
    public AlarmHandlerTimeoutRateMetricDTO getValAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(VAL_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    /**
     * 漏电报警处理效率指标
     */
    @MetricTopic(topic = "LCA_ALARM_HANDLER_TIMEOUT_EFFICIENCY", description = "漏电报警报警处理率指标")
    public AlarmHandlerTimeoutRateMetricDTO getLcaAlarmHandlerTimeoutEfficiencyMetric(AlarmMetricParam param) {
        AlarmBimStatisticsQueryParam alarmBimStatisticsQueryParam = getAlarmBimStatisticsQueryParam(param);
        alarmBimStatisticsQueryParam.setAlarmEventTypeIds(Collections.singleton(LCA_EVENT_CODE));
        return getCommonAlarmHandlerTimeoutEfficiencyMetric(alarmBimStatisticsQueryParam);
    }

    private AlarmHandlerTimeoutRateMetricDTO getCommonAlarmHandlerTimeoutEfficiencyMetric(AlarmBimStatisticsQueryParam param) {
        //总超时占比数据
        AlarmHandlerTimeoutRateStatisticsVO alarmHandlerTimeoutRate = this.alarmBimStatisticsClient.getAlarmHandlerTimeoutRate(param);
        AlarmHandlerTimeoutRateMetricDTO timeoutRateMetricDTO = AlarmHandlerTimeoutRateMetricDTO.builder()
                .totalHandlerNum(alarmHandlerTimeoutRate.getTotalHandlerNum())
                .onTimeHandlerNum(alarmHandlerTimeoutRate.getTotalHandlerNum() - alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum())
                .onTimeHandlerRate(
                        Objects.isNull(alarmHandlerTimeoutRate.getTotalHandlerNum()) || 0 == alarmHandlerTimeoutRate.getTotalHandlerNum()
                        ? 0 :
                        Objects.isNull(alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum()) ||
                        0 == alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum() ? 100 : 100 -
                                                                                         alarmHandlerTimeoutRate
                                                                                                 .getTotalHandlerTimeoutRate()
                                                                                                 .doubleValue())
                .totalHandlerTimeoutNum(alarmHandlerTimeoutRate.getTotalHandlerTimeoutNum())
                .build();
        //用户超时占比数据
        List<AlarmUserHandlerTimeoutRateStatisticsVO> alarmUserHandlerTimeoutRate = this.alarmBimStatisticsClient.getAlarmUserHandlerTimeoutRate(param);
        List<AlarmHandlerTimeoutRateMetricDTO.Users> users = alarmUserHandlerTimeoutRate.stream()
                .map(user ->
                             AlarmHandlerTimeoutRateMetricDTO.Users.builder()
                                     .userId(user.getUserId())
                                     .username(user.getUsername())
                                     .totalHandlerNum(user.getTotalHandlerNum())
                                     .onTimeHandlerNum(user.getTotalHandlerNum() - user.getTotalHandlerTimeoutNum())
                                     .onTimeHandlerRate(Objects.isNull(user.getTotalHandlerNum()) || 0 == user.getTotalHandlerNum() ? 0 :
                                                        Objects.isNull(user.getTotalHandlerTimeoutRate()) ||
                                                        0 == user.getTotalHandlerTimeoutRate().doubleValue() ? 100 : 100 - user
                                                                .getTotalHandlerTimeoutRate().doubleValue())
                                     .totalHandlerTimeoutNum(user.getTotalHandlerTimeoutNum())
                                     .build()
                    ).collect(Collectors.toList());
        timeoutRateMetricDTO.setTopUsers(users.stream()
                                                 .sorted(Comparator.comparing(AlarmHandlerTimeoutRateMetricDTO.Users::getOnTimeHandlerRate)
                                                                 .reversed())
                                                 .limit(5)
                                                 .collect(Collectors.toList()));
        timeoutRateMetricDTO.setLowUsers(users.stream()
                                                 .sorted(Comparator.comparing(AlarmHandlerTimeoutRateMetricDTO.Users::getOnTimeHandlerRate))
                                                 .limit(5)
                                                 .collect(Collectors.toList()));
        return timeoutRateMetricDTO;
    }

    /**
     * 处理率排名指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ALARM_USER_HANDLER_RATE_RANK_METRIC", description = "总报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getAlarmUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, null));
    }

    /**
     * 一级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "ONE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "一级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getOneLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, ONE_LEVEL_CODE, null));
    }

    /**
     * 二级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "TWO_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "二级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getTwoLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, TWO_LEVEL_CODE, null));
    }

    /**
     * 三级级报警处理率指标排名
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "THREE_LEVEL_ALARM_HANDLER_RATE_RANK_METRIC", description = "三级报警处理率指标排名")
    public AlarmUserHandlerRateTopMetricDTO getThreeLevelUserHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, THREE_LEVEL_CODE, null));
    }

    /**
     * 明火烟雾报警处理率指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "FLAME_SMOG_ALARM_HANDLER_RATE_RANK_METRIC", description = "明火烟雾报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getFlameSmogAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, FlAME_SMOG_EVENT_CODES));
    }

    /**
     * 违章占道报警处理率指标
     */
    @MetricTopic(topic = "ILLEGAL_PARKING_ALARM_HANDLER_RATE_RANK_METRIC", description = "违章占道报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getIllegalParkingAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(ILOR_EVENT_CODE)));
    }

    /**
     * 交通拥堵报警处理率指标
     */
    @MetricTopic(topic = "TRAFFIC_JAM_ALARM_HANDLER_RATE_RANK_METRIC", description = "交通拥堵报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getTrafficJamAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(TC_EVENT_CODE)));
    }

    /**
     * 水压报警处理率指标
     */
    @MetricTopic(topic = "WATER_PRESSURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "水压报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getWaterPressureAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(WP_EVENT_CODE)));
    }

    /**
     * 手报报警
     */
    @MetricTopic(topic = "HAND_ALARM_HANDLER_RATE_RANK_METRIC", description = "手报报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getHandAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(MCP_EVENT_CODE)));
    }

    /**
     * UPS报警
     */
    @MetricTopic(topic = "UPS_ALARM_HANDLER_RATE_RANK_METRIC", description = "UPS报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getUpsAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(UPS_EVENT_CODE)));
    }

    /**
     * 温度报警
     */
    @MetricTopic(topic = "TEMPERATURE_ALARM_HANDLER_RATE_RANK_METRIC", description = "温度报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getTemperatureAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(TEMPERATURE_EVENT_CODE)));
    }

    /**
     * 违规占用消防通道
     */
    @MetricTopic(topic = "UPFL_ALARM_HANDLER_RATE_RANK_METRIC", description = "违规占用消防通道报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getUpflAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(UPFL_EVENT_CODE)));
    }

    /**
     * 温湿度报警
     */
    @MetricTopic(topic = "WSD_ALARM_HANDLER_RATE_RANK_METRIC", description = "温湿度报警报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getWSDAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(WSD_EVENT_CODE)));
    }

    /**
     * 电压报警
     */
    @MetricTopic(topic = "VAL_ALARM_HANDLER_RATE_RANK_METRIC", description = "电压报警报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getValAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(VAL_EVENT_CODE)));
    }

    /**
     * 漏电报警
     */
    @MetricTopic(topic = "LCA_ALARM_HANDLER_RATE_RANK_METRIC", description = "漏电报警报警处理率指标")
    public AlarmUserHandlerRateTopMetricDTO getLcaAlarmHandlerRateRankMetric(MetricParam param) {
        return getAlarmUserHandlerRateTopMetricDTO(this.convertQueryParam(param, null, Collections.singleton(LCA_EVENT_CODE)));
    }

    /**
     * 请求参数convert
     *
     * @param param      参数
     * @param level      报警级别
     * @param eventTypes 事件类型
     *
     * @return AlarmStatisticsQueryParam
     */
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
    private AlarmUserHandlerRateTopMetricDTO getAlarmUserHandlerRateTopMetricDTO(AlarmStatisticsQueryParam queryParam) {
        //报警数
        AlarmBasicAnalysisStatisticsDTO statisticsDTO = this.alarmStatisticsClient.getAlarmBasicAnalysisNumberStatistics(queryParam);
        queryParam.setPageSize(5);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateTopStatistics(queryParam);
        List<AlarmUserHandlerRateTopVO> alarmUserHandlerRateLastTopStatistics = this.alarmStatisticsClient.getAlarmUserHandlerRateLastTopStatistics(queryParam);
        List<AlarmUserHandlerRateTopMetricDTO.UserDetails> topUserDetails = alarmUserHandlerRateTopStatistics.stream().map(top ->
                                                                                                                                   AlarmUserHandlerRateTopMetricDTO.UserDetails
                                                                                                                                           .builder()
                                                                                                                                           .userHandlerAlarmNumber(top
                                                                                                                                                                           .getUserHandlerAlarmNumber()
                                                                                                                                                                           .intValue())
                                                                                                                                           .alarmHandlerUserName(top.getAlarmHandlerUserName())
                                                                                                                                           .userHandlerRate(top.getUserHandlerRate())
                                                                                                                                           .build()
                                                                                                                          )
                .collect(Collectors.toList());
        List<AlarmUserHandlerRateTopMetricDTO.UserDetails> lowUserDetails = alarmUserHandlerRateLastTopStatistics.stream().map(top ->
                                                                                                                                       AlarmUserHandlerRateTopMetricDTO.UserDetails
                                                                                                                                               .builder()
                                                                                                                                               .userHandlerAlarmNumber(top
                                                                                                                                                                               .getUserHandlerAlarmNumber()
                                                                                                                                                                               .intValue())
                                                                                                                                               .alarmHandlerUserName(top.getAlarmHandlerUserName())
                                                                                                                                               .userHandlerRate(top.getUserHandlerRate())
                                                                                                                                               .build()
                                                                                                                              )
                .collect(Collectors.toList());
        return AlarmUserHandlerRateTopMetricDTO.builder()
                .totalAlarmNumber(statisticsDTO.getAlarmNum())
                .handlerAlarmNumber(statisticsDTO.getHandlerNum())
                .handlerRate(Objects.isNull(statisticsDTO.getAlarmNum()) || 0 == statisticsDTO.getAlarmNum() ? 0 : BigDecimal
                        .valueOf(statisticsDTO.getHandlerNum())
                        .divide(BigDecimal.valueOf(statisticsDTO.getAlarmNum()), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue())
                .topHandlerUsers(topUserDetails)
                .lowHandlerUsers(lowUserDetails)
                .build();
    }
}
