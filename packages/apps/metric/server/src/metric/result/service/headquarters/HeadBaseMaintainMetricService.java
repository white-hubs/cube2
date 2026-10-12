package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenWorkOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordUserNumVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddendangerWordOrderStatisticsVO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.FAULT_REPAIR_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.HIDDEN_DANGER_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_EXCEPTION_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_RECORD_USER_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_NUMBER_TIME_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_TOTAL_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/16 14:40
 * @Description:
 */
@Service
public class HeadBaseMaintainMetricService {
    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadBaseMaintainMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    /**
     * 总部隐患整改总数指标
     */
    @MetricTopic(topic = "HEAD_BASE_HIDDEN_DANGER_NUMBER", description = "总部隐患整改总数指标")
    public Integer hiddenDangerNum(MetricParam param) {
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> allMarketHiddenMetrics = asyncHttpClientUtils.getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL,
                                                                                                                                HttpMethod.GET, null, param,
                                                                                                                                new TypeReference<HiddenDangerNumStatisticsVO>() {});
        Integer totalNum = 0;
        for (MarketMetricResult<HiddenDangerNumStatisticsVO> allMarketHiddenMetric : allMarketHiddenMetrics) {
            if (Objects.nonNull(allMarketHiddenMetric.getMetricStruct())) {
                totalNum += allMarketHiddenMetric.getMetricStruct().getTotal();
            }
        }
        return totalNum;
    }

    /**
     * 总部隐患整改数完成数指标
     */
    @MetricTopic(topic = "HEAD_BASE_HIDDEN_DANGER_COMPLETED_RATE", description = "总部隐患整改数完成数指标")
    public Integer hiddenDangerCompletedRate(MetricParam param) {
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> allMarketHiddenMetrics = asyncHttpClientUtils.getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL,
                                                                                                                                HttpMethod.GET, null, param,
                                                                                                                                new TypeReference<HiddenDangerNumStatisticsVO>() {});
        Integer completeNum = 0;
        for (MarketMetricResult<HiddenDangerNumStatisticsVO> allMarketHiddenMetric : allMarketHiddenMetrics) {
            if (Objects.nonNull(allMarketHiddenMetric.getMetricStruct())) {
                completeNum += allMarketHiddenMetric.getMetricStruct().getFinished();
            }
        }
        return completeNum;
    }

    /**
     * 总部隐患整改处理率最高的隐患类型
     */
    @MetricTopic(topic = "HEAD_BASE_HIDDEN_DANGER_COMPLETED_RATE_RANK_METRIC", description = "总部隐患整改处理率最高的隐患类型")
    public List<HandlerRateDTO> hiddenDangerCompletedRateRankMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "HIDDENDANGER", Comparator.comparing(HandlerRateDTO::getRate).reversed());
    }

    private List<HandlerRateDTO> getHandlerRateDTOS(MetricParam param, String businessType,
                                                    Comparator<HandlerRateDTO> reversed
                                                   ) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<MarketMetricResult<List<HiddendangerWordOrderStatisticsVO>>> hiddendangerWordOrderStatisticsVOS = asyncHttpClientUtils
                .getAllMarketMetrics(WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<List<HiddendangerWordOrderStatisticsVO>>() {});

        return hiddendangerWordOrderStatisticsVOS.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    for (HiddendangerWordOrderStatisticsVO statistics : t.getMetricStruct()) {
                        statistics.setMarketName(t.getMarketName());
                    }
                    return t.getMetricStruct();
                }).flatMap(Collection::stream)
                .map(t -> {
                    double rate = 0;
                    if (0 != t.getTotalNumber()) {
                        rate = BigDecimal.valueOf(t.getDealNumber())
                                .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue();
                    }
                    return HandlerRateDTO.builder()
                            .marketName(t.getMarketName())
                            .num(Long.valueOf(t.getDealNumber()))
                            .name(t.getHiddenTypeName())
                            .rate(rate)
                            .build();
                })
                .sorted(reversed)
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部隐患整改处理率最低的隐患类型
     */

    @MetricTopic(topic = "HEAD_BASE_HIDDEN_DANGER_COMPLETED_RATE_BOTTOM_METRIC", description = "总部隐患整改处理率最低的隐患类型")
    public List<HandlerRateDTO> hiddenDangerCompletedRateBottomMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "HIDDENDANGER", Comparator.comparing(HandlerRateDTO::getRate));
    }

    /**
     * 总部故障报修总数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_NUMBER", description = "总部故障报修总数")
    public Integer faultNumberMetic(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});

        Integer total = 0;
        for (MarketMetricResult<FaultCompletedMetricVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                total += marketMetricResult.getMetricStruct().getTotal();
            }
        }
        return total;
    }

    /**
     * 总部故障报修完成数
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_COMPLETED_RATE", description = "总部故障报修完成数")
    public Integer faultOrderCompletedRate(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});

        Integer finished = 0;
        for (MarketMetricResult<FaultCompletedMetricVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                finished += marketMetricResult.getMetricStruct().getCompleted();
            }
        }
        return finished;
    }

    /**
     * 总部故障报修处理率最高的故障类型
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_COMPLETED_RATE_RANK_METRIC", description = "总部故障报修处理率最高的故障类型")
    public List<HandlerRateDTO> faultOrderRateRankMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "FAULT", Comparator.comparing(HandlerRateDTO::getRate).reversed());
    }

    /**
     * 总部故障报修处理率最低的故障类型
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_COMPLETED_RATE_BOTTOM_METRIC", description = "总部故障报修处理率最低的故障类型")
    public List<HandlerRateDTO> faultOrderRateBottomMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "FAULT", Comparator.comparing(HandlerRateDTO::getRate));

    }

    /**
     * 故障报修处理率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_COMPLETED_RATE_TOP_MARKET", description = "故障报修处理率最高的五个市场")
    public List<HandlerRateDTO> faultOrderRateTopMetric(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateMarketList(param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getHandlerRateMarketList(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});

        List<HandlerRateDTO> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<FaultCompletedMetricVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                marketCompletedRateMetrics.add(HandlerRateDTO.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .num(Long.valueOf(marketMetricResult.getMetricStruct().getCompleted()))
                                                       .rate(marketMetricResult.getMetricStruct().getRate())
                                                       .build());
            }
        }
        return marketCompletedRateMetrics;
    }

    /**
     * 故障报修处理率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_FAULT_ORDER_COMPLETED_RATE_BOTTOM_MARKET", description = "故障报修处理率最低的五个市场")
    public List<HandlerRateDTO> faultOrderRateBottomMarket(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHandlerRateMarketList(param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 隐患排查处理率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_HIDDENDANGER_COMPLETED_RATE_TOP_MARKET", description = "隐患排查处理率最高的五个市场")
    public List<HandlerRateDTO> hiddenDangerOrderRateTopMarket(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHiddenDangerHandlerRateMarketList(param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getHiddenDangerHandlerRateMarketList(MetricParam param) {
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HiddenDangerNumStatisticsVO>() {});

        List<HandlerRateDTO> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<HiddenDangerNumStatisticsVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                marketCompletedRateMetrics.add(HandlerRateDTO.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .num(Long.valueOf(marketMetricResult.getMetricStruct().getFinished()))
                                                       .rate(marketMetricResult.getMetricStruct().getCalculateProportion())
                                                       .build());
            }
        }
        return marketCompletedRateMetrics;
    }

    /**
     * 隐患排查处理率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_HIDDENDANGER_COMPLETED_RATE_BOTTOM_MARKET", description = "隐患排查处理率最低的五个市场")
    public List<HandlerRateDTO> hiddenDangerOrderRateBottomMarket(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getHiddenDangerHandlerRateMarketList(param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检总任务数
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_NUMBER", description = "总部日常巡检总任务数")
    public Long inspectTaskNumber(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionCompleteMetric : allMarketInspectionCompleteMetrics) {
            if (Objects.nonNull(allMarketInspectionCompleteMetric.getMetricStruct())) {
                totalNum += allMarketInspectionCompleteMetric.getMetricStruct().getTotal();
            }
        }
        return totalNum;
    }

    /**
     * 总部日常巡检任务完成数
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_COMPLETE_NUMBER", description = "总部日常巡检任务完成数")
    public Long inspectTaskCompleteNumber(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long completeNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionCompleteMetric : allMarketInspectionCompleteMetrics) {
            if (Objects.nonNull(allMarketInspectionCompleteMetric.getMetricStruct())) {
                completeNum += allMarketInspectionCompleteMetric.getMetricStruct().getNumber();
            }
        }

        return completeNum;
    }

    /**
     * 总部日常巡检异常数
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_ERROR_NUM", description = "总部日常巡检异常数")
    public Long inspectionRecordErrorNum(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionErrorMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long errorNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionErrorMetric : allMarketInspectionErrorMetrics) {
            if (Objects.nonNull(allMarketInspectionErrorMetric.getMetricStruct())) {
                errorNum += allMarketInspectionErrorMetric.getMetricStruct().getNumber();
            }
        }
        return errorNum;
    }

    /**
     * 总部日常巡检异常率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_ERROR_RATE_TOP_MARKET", description = "总部日常巡检异常率最高的五个市场")
    public List<HandlerRateDTO> inspectionRecordErrorRateTopMarket(MetricParam param) {
        List<HandlerRateDTO> errorRateMarketList = getInspectHandlerRateList(INSPECTION_EXCEPTION_RATE_REQUEST_URL, param);
        return errorRateMarketList.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检异常率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_ERROR_RATE_BOTTOM_MARKET", description = "总部日常巡检异常率最低的五个市场")
    public List<HandlerRateDTO> inspectionRecordErrorRateBottomMarket(MetricParam param) {
        List<HandlerRateDTO> errorRateMarketList = getInspectHandlerRateList(INSPECTION_EXCEPTION_RATE_REQUEST_URL, param);
        return errorRateMarketList.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理率最高的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_COMPLETE_RATE_TOP_USER", description = "总部日常巡检处理率最高的五个人")
    public List<HandlerRateDTO> inspectTaskCompleteRateTopUser(MetricParam param) {
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});

        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerRateUser().forEach(user ->
                                                                                user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerRateUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理率最低的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_COMPLETE_RATE_BOTTOM_USER", description = "总部日常巡检处理率最低的五个人")
    public List<HandlerRateDTO> inspectTaskCompleteRateBottomUser(MetricParam param) {
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});

        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerRateUser().forEach(user ->
                                                                                   user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerRateUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_COMPLETE_RATE_TOP_MARKET", description = "总部日常巡检处理率最高的五个市场")
    public List<HandlerRateDTO> inspectTaskCompleteRateTopMarket(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getInspectHandlerRateList(INSPECTION_COMPLETE_RATE_REQUEST_URL, param);
        return marketCompletedRateMetrics.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理率最低的五个市场
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_COMPLETE_RATE_BOTTOM_MARKET", description = "总部日常巡检处理率最低的五个市场")
    public List<HandlerRateDTO> inspectTaskCompleteRateBottomMarket(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getInspectHandlerRateList(INSPECTION_COMPLETE_RATE_REQUEST_URL, param);
        return marketCompletedRateMetrics.stream().sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检总处理时长
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_HANDLER_TIME", description = "总部日常巡检总处理时长")
    public Long inspectTaskHandlerTime(MetricParam param) {
        List<MarketMetricResult<TaskHandlerTimeVO>> marketTimeMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TaskHandlerTimeVO>() {});
        Long taskTotalTime = 0L;
        for (MarketMetricResult<TaskHandlerTimeVO> metricResult : marketTimeMetricResultList) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                taskTotalTime += metricResult.getMetricStruct().getTaskTotalTime();
            }
        }
        return taskTotalTime;
    }

    /**
     * 总部日常巡检处理效率最高的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_HANDLER_EFFICIENCY_TOP_USER", description = "总部日常巡检处理效率最高的五个人")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyTopUser(MetricParam param) {
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerEfficiencyUser().forEach(user ->
                                                                                      user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理效率最低的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_HANDLER_EFFICIENCY_BOTTOM_USER", description = "总部日常巡检处理效率最低的五个人")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyBottomUser(MetricParam param) {
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerEfficiencyUser().forEach(user ->
                                                                                         user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理效率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_HANDLER_EFFICIENCY_TOP_MARKET", description = "总部日常巡检处理效率最高的五个市场")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyTopMarket(MetricParam param) {
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = getHandlerEfficiencyMarket(INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL, param);
        return handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理效率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_TASK_HANDLER_EFFICIENCY_BOTTOM_MARKET", description = "总部日常巡检处理效率最低的五个市场")
    public List<HandlerEfficiencyDTO> inspectTaskHandlerEfficiencyBottomMarket(MetricParam param) {
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = getHandlerEfficiencyMarket(INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL, param);
        return handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检次数
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_NUM_METRIC", description = "总部日常巡检次数")
    public Long inspectionRecordNumMetric(MetricParam param) {
        List<MarketMetricResult<InspectionRecordNumMetricDTO>> marketNumberMetricResults = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_RECORD_USER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<InspectionRecordNumMetricDTO>() {});
        Long inspectNum = 0L;
        for (MarketMetricResult<InspectionRecordNumMetricDTO> metricResult : marketNumberMetricResults) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                inspectNum += metricResult.getMetricStruct().getInspectionRecordNum();
            }
        }
        return inspectNum;
    }

    /**
     * 总部日常巡检处理数最高的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_HANDLER_NUM_TOP_USER", description = "总部日常巡检处理数最高的五个人")
    public List<InspectionRecordUserNumVO> inspectionRecordHandlerNumTopUser(MetricParam param) {
        List<MarketMetricResult<InspectionRecordNumMetricDTO>> marketNumberMetricResults = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_RECORD_USER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<InspectionRecordNumMetricDTO>() {});

        return marketNumberMetricResults.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopInspectUser().forEach(user ->
                                                                            user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopInspectUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum).reversed()).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检处理数最低的五个人
     */

    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_HANDLER_NUM_BOTTOM_USER", description = "总部日常巡检处理数最低的五个人")
    public List<InspectionRecordUserNumVO> inspectionRecordHandlerNumBottomUser(MetricParam param) {
        List<MarketMetricResult<InspectionRecordNumMetricDTO>> marketNumberMetricResults = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_RECORD_USER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<InspectionRecordNumMetricDTO>() {});

        return marketNumberMetricResults.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomInspectUser().forEach(user ->
                                                                               user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomInspectUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部日常巡检次数最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_NUM_TOP_MARKET", description = "总部日常巡检次数最高的五个市场")
    public List<InspectionRecordUserNumVO> inspectionRecordNumTopMarket(MetricParam param) {
        List<InspectionRecordUserNumVO> inspectionRecordUserNumVOS = getInspectionRecordUserNumMarket(param);
        return inspectionRecordUserNumVOS.stream().sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部日常巡检次数最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_INSPECT_RECORD_NUM_BOTTOM_MARKET", description = "总部日常巡检次数最低的五个市场")
    public List<InspectionRecordUserNumVO> inspectionRecordNumBottomMarket(MetricParam param) {
        List<InspectionRecordUserNumVO> inspectionRecordUserNumVOS = getInspectionRecordUserNumMarket(param);
        return inspectionRecordUserNumVOS.stream().sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    private List<InspectionRecordUserNumVO> getInspectionRecordUserNumMarket(MetricParam param) {
        List<MarketMetricResult<InspectionRecordNumMetricDTO>> marketNumberMetricResults = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_RECORD_USER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<InspectionRecordNumMetricDTO>() {});
        List<InspectionRecordUserNumVO> inspectionRecordUserNumVOS = new ArrayList<>();
        for (MarketMetricResult<InspectionRecordNumMetricDTO> metricResult : marketNumberMetricResults) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                InspectionRecordUserNumVO inspectionRecord = new InspectionRecordUserNumVO();
                inspectionRecord.setMarketName(metricResult.getMarketName());
                inspectionRecord.setNum(metricResult.getMetricStruct().getInspectionRecordNum());
                inspectionRecordUserNumVOS.add(inspectionRecord);
            }
        }
        return inspectionRecordUserNumVOS;
    }

    /**
     * 总部电子巡更数
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_NUM_METRIC", description = "总部电子巡更数")
    public Long electronNumMetric(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronTotalMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_TOTAL_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        return allMarketElectronTotalMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getTotal())
                .reduce(Long::sum).orElse(0L);
    }

    /**
     * 总部电子巡更处理数
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_COMPLETE_NUM_METRIC", description = "总部电子巡更处理数")
    public Long electronCompleteNumMetric(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> marketElectronMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long completeNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketElectronCompleteMetric : marketElectronMetricResultList) {
            if (Objects.nonNull(allMarketElectronCompleteMetric.getMetricStruct())) {
                completeNum += allMarketElectronCompleteMetric.getMetricStruct().getNumber();
            }
        }
        return completeNum;
    }

    /**
     * 总部电子巡更处理率最高的五个部门
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_RATE_TOP_METRIC", description = "总部电子巡更处理率最高的五个部门")
    public List<HandlerRateDTO> electronHandleRateTopMetric(MetricParam param) {
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});

        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerRateDept().forEach(user ->
                                                                                user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerRateDept();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理率最低的五个部门
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_RATE_BOTTOM_METRIC", description = "总部电子巡更处理率最低的五个部门")
    public List<HandlerRateDTO> electronHandleRateBottomMetric(MetricParam param) {
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});

        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerRateDept().forEach(user ->
                                                                                   user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerRateDept();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_RATE_TOP_MARKET_METRIC", description = "总部电子巡更处理率最高的五个市场")
    public List<HandlerRateDTO> electronHandleRateTopMarketMetric(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getInspectHandlerRateList(PATROL_COMPLETE_RATE_REQUEST_URL, param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_RATE_BOTTOM_MARKET_METRIC", description = "总部电子巡更处理率最低的五个市场")
    public List<HandlerRateDTO> electronHandleRateBottomMarketMetric(MetricParam param) {
        List<HandlerRateDTO> marketCompletedRateMetrics = getInspectHandlerRateList(PATROL_COMPLETE_RATE_REQUEST_URL, param);
        return marketCompletedRateMetrics.stream()
                .sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerRateDTO> getInspectHandlerRateList(String url, MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> marketElectronMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(url, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});

        List<HandlerRateDTO> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<TotalNumberRateDTO> allMarketElectronCompleteMetric : marketElectronMetricResultList) {
            if (Objects.nonNull(allMarketElectronCompleteMetric.getMetricStruct())) {
                marketCompletedRateMetrics.add(HandlerRateDTO.builder()
                                                       .marketName(allMarketElectronCompleteMetric.getMarketName())
                                                       .num(allMarketElectronCompleteMetric.getMetricStruct().getNumber())
                                                       .rate(allMarketElectronCompleteMetric.getMetricStruct().getRate().doubleValue())
                                                       .build());
            }
        }
        return marketCompletedRateMetrics;
    }

    /**
     * 总部电子巡更总处理时长
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_TIME_TOTAL_METRIC", description = "总部电子巡更总处理时长")
    public Long electronHandleTimeTotalMetric(MetricParam param) {
        List<MarketMetricResult<TaskHandlerTimeVO>> marketTimeMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_NUMBER_TIME_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TaskHandlerTimeVO>() {});

        Long taskTotalTime = 0L;
        for (MarketMetricResult<TaskHandlerTimeVO> metricResult : marketTimeMetricResultList) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                taskTotalTime += metricResult.getMetricStruct().getTaskTotalTime();
            }
        }
        return taskTotalTime;
    }

    /**
     * 总部电子巡更处理效率最高的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_EFFICIENCY_RANK_METRIC", description = "总部电子巡更处理效率指标")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyRankMetric(MetricParam param) {
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerEfficiencyUser().forEach(user ->
                                                                                      user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理效率最低的五个人
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_EFFICIENCY_BOTTOM_METRIC", description = "总部电子巡更处理效率最低的五个人")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyBottomMetric(MetricParam param) {
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        return marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerEfficiencyUser().forEach(user ->
                                                                                         user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5).collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理效率最高的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_EFFICIENCY_TOP_MARKET_METRIC", description = "总部电子巡更处理效率最高的五个市场")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyTopMarketMetric(MetricParam param) {
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = getHandlerEfficiencyMarket(PATROL_HANDLER_NUMBER_TIME_REQUEST_URL, param);
        return handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 总部电子巡更处理效率最低的五个市场
     */
    @MetricTopic(topic = "HEAD_BASE_ELECTRON_HANDLE_EFFICIENCY_BOTTOM_MARKET_METRIC", description = "总部电子巡更处理效率最低的五个市场")
    public List<HandlerEfficiencyDTO> electronHandleEfficiencyBottomMarketMetric(MetricParam param) {
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = getHandlerEfficiencyMarket(PATROL_HANDLER_NUMBER_TIME_REQUEST_URL, param);
        return handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5)
                .collect(Collectors.toList());
    }

    private List<HandlerEfficiencyDTO> getHandlerEfficiencyMarket(String url, MetricParam param) {
        List<MarketMetricResult<TaskHandlerTimeVO>> marketTimeMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(url, HttpMethod.GET, null, param, new TypeReference<TaskHandlerTimeVO>() {});

        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = new ArrayList<>();
        for (MarketMetricResult<TaskHandlerTimeVO> metricResult : marketTimeMetricResultList) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {

                HandlerEfficiencyDTO handlerEfficiencyDTO = new HandlerEfficiencyDTO();
                handlerEfficiencyDTO.setMarketName(metricResult.getMarketName());
                handlerEfficiencyDTO.setAvgTime(0 != metricResult.getMetricStruct().getTaskHandlerNumber() ?
                                                BigDecimal.valueOf(metricResult.getMetricStruct().getTaskTotalTime())
                                                        .divide(BigDecimal.valueOf(metricResult.getMetricStruct()
                                                                                           .getTaskHandlerNumber()), 2, RoundingMode.HALF_UP)
                                                        .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                        .intValue() : 0L);
                handlerEfficiencyDTOS.add(handlerEfficiencyDTO);
            }
        }
        return handlerEfficiencyDTOS;
    }
}
