package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.base.dto.MarketMetricResult;
import com.guhecloud.cube.biz.metric.server.metric.result.base.utils.AsyncHttpClientUtils;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ElectronMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.InspectionMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.param.mcp.HeadFaultMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.ElectronTotalMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto.PatrolTaskRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto.FaultOrderCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto.FaultOrderNumberMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.looseworks.MarketCompletedRateMetric;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadElectronHandlerEfficiencyRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadElectronHandlerRateRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadInspectionHandlerEfficiencyRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadInspectionHandlerRateRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadInspectionRecordNumMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadWorkOrderCompletedRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.maintain.HeadWorkOrderEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HandlerTypeRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerNumMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionCompleteRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionErrorRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto.InspectionTotalMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenWorkOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordUserNumVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.ElectronQueryParam;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.FAULT_REPAIR_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.HIDDEN_DANGER_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_EXCEPTION_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_RECORD_USER_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.INSPECTION_TOTAL_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_COMPLETE_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_EXCEPTION_RATE_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_NUMBER_TIME_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_HANDLER_RATE_TOP_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.PATROL_TOTAL_NUMBER_REQUEST_URL;
import static com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants.HeadquartersMetricConstants.WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL;

/**
 * 总部运维指标服务（故障、隐患、巡检、巡更）
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 10:07
 * @Description:
 */

@Service
public class HeadquartersMaintainMetricService {

    private final AsyncHttpClientUtils asyncHttpClientUtils;

    public HeadquartersMaintainMetricService(AsyncHttpClientUtils asyncHttpClientUtils) {
        this.asyncHttpClientUtils = asyncHttpClientUtils;
    }

    /**
     * 故障工单数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "HEADQUARTERS_HIDDEN_DANGER_NUMBER", description = "隐患整改总数指标")
    public HiddenDangerNumMetricDTO hiddenDangerNum(MetricParam param) {

        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> allMarketHiddenMetrics = asyncHttpClientUtils.getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL,
                                                                                                                                HttpMethod.GET, null, param, new TypeReference<HiddenDangerNumStatisticsVO>() {});
        Integer result = allMarketHiddenMetrics.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> t.getMetricStruct().getTotal())
                .reduce(Integer::sum).orElse(0);
        return HiddenDangerNumMetricDTO.builder()
                .hiddenDangerNumber(result)
                .build();
    }

    /**
     * 隐患整改指标
     */
    @MetricTopic(topic = "HEADQUARTERS_HIDDEN_DANGER_COMPLETED_RATE", description = "隐患整改完成率指标")
    public HiddenDangerCompletedRateMetricDTO hiddenDangerCompletedRate(MetricParam param) {
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> allMarketHiddenMetrics = asyncHttpClientUtils.getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL,
                                                                                                                                HttpMethod.GET, null, param,
                                                                                                                                new TypeReference<HiddenDangerNumStatisticsVO>() {});
        Integer totalNum = 0;
        Integer completeNum = 0;

        for (MarketMetricResult<HiddenDangerNumStatisticsVO> allMarketHiddenMetric : allMarketHiddenMetrics) {
            if (Objects.nonNull(allMarketHiddenMetric.getMetricStruct())) {
                completeNum += allMarketHiddenMetric.getMetricStruct().getFinished();
                totalNum += allMarketHiddenMetric.getMetricStruct().getTotal();
            }
        }
        HiddenDangerNumStatisticsVO result = new HiddenDangerNumStatisticsVO();
        result.setTotal(totalNum);
        result.setFinished(completeNum);
        return HiddenDangerCompletedRateMetricDTO.builder()
                .hiddenDangerNumber(totalNum)
                .hiddenDangerCompletedNumber(result.getFinished())
                .hiddenDangerCompletedRate(result.getCalculateProportion())
                .build();
    }

    /**
     * 故障报修指标
     */
    @MetricTopic(topic = "HEADQUARTERS_FAULT_ORDER_NUMBER", description = "故障报修总数指标")
    public FaultOrderNumberMetricDTO faultNumberMetic(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> allMarketFaultMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});
        Integer totalNum = 0;
        for (MarketMetricResult<FaultCompletedMetricVO> allMarketHiddenMetric : allMarketFaultMetrics) {
            if (Objects.nonNull(allMarketHiddenMetric.getMetricStruct())) {
                totalNum += allMarketHiddenMetric.getMetricStruct().getTotal();
            }
        }
        return FaultOrderNumberMetricDTO.builder()
                .faultRepairNumber(totalNum)
                .build();
    }

    /**
     * 故障保修指标
     */
    @MetricTopic(topic = "HEADQUARTERS_FAULT_ORDER_COMPLETED_RATE", description = "故障报修完成率指标")
    public FaultOrderCompletedRateMetricDTO faultOrderCompletedRate(HeadFaultMetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> allMarketFaultMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});
        Integer totalNum = 0;
        Integer completeNum = 0;

        for (MarketMetricResult<FaultCompletedMetricVO> allMarketHiddenMetric : allMarketFaultMetrics) {
            if (Objects.nonNull(allMarketHiddenMetric.getMetricStruct())) {
                completeNum += allMarketHiddenMetric.getMetricStruct().getCompleted();
                totalNum += allMarketHiddenMetric.getMetricStruct().getTotal();
            }
        }
        return FaultOrderCompletedRateMetricDTO.builder()
                .faultRepairNumber(totalNum)
                .faultRepairCompletedNumber(completeNum)
                .faultRepairCompletedRate(BigDecimal.valueOf(completeNum)
                                                  .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    /**
     * 隐患整改处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_HIDDEN_DANGER_DEAL_RATE_RANK_METRIC", description = "隐患整改处理率指标")
    public HeadWorkOrderCompletedRankMetricDTO headquartersHiddenDangerDealRate(MetricParam param) {
        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HiddenDangerNumStatisticsVO>() {});

        Integer total = 0;
        Integer finished = 0;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<HiddenDangerNumStatisticsVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                total += marketMetricResult.getMetricStruct().getTotal();
                finished += marketMetricResult.getMetricStruct().getFinished();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .number(Long.valueOf(marketMetricResult.getMetricStruct().getFinished()))
                                                       .rate(marketMetricResult.getMetricStruct().getCalculateProportion())
                                                       .build());
            }
        }
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = new HiddenDangerNumStatisticsVO();
        hiddenDangerNumStatisticsVO.setTotal(total);
        hiddenDangerNumStatisticsVO.setFinished(finished);
        HeadWorkOrderCompletedRankMetricDTO hiddendanger = getOrderCompletedRankMetricDTO(param, "HIDDENDANGER");
        hiddendanger.setRepairCount(total);
        hiddendanger.setHandleCount(finished);
        hiddendanger.setHandleRate(hiddenDangerNumStatisticsVO.getCalculateProportion());
        hiddendanger.setTopMarketMetric(marketCompletedRateMetrics.stream()
                                                .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed())
                                                .limit(5).collect(Collectors.toList()));
        hiddendanger.setLowMarketMetric(marketCompletedRateMetrics.stream().sorted(Comparator.comparing(MarketCompletedRateMetric::getRate))
                                                .limit(5).collect(Collectors.toList()));
        return hiddendanger;
    }

    /**
     * 隐患整改处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_HIDDEN_DANGER_DEAL_EFFICIENCY_RANK_METRIC", description = "隐患整改处理效率指标")
    public HeadWorkOrderEfficiencyMetricDTO headquartersHiddenDangerDealEfficiencyRankMetric(MetricParam param) {

        List<MarketMetricResult<HiddenDangerNumStatisticsVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(HIDDEN_DANGER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HiddenDangerNumStatisticsVO>() {});
        Map<Long, Long> marketHandlerTimeMap = new HashMap<>();
        HeadWorkOrderEfficiencyMetricDTO hiddendanger = getOrderEfficiencyMetricDTO(param, "HIDDENDANGER", marketHandlerTimeMap);
        Integer totalFinishNum = 0;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<HiddenDangerNumStatisticsVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                totalFinishNum += marketMetricResult.getMetricStruct().getTotal();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .number(Long.valueOf(marketMetricResult.getMetricStruct().getFinished()))
                                                       .rate(0 != marketMetricResult.getMetricStruct().getFinished() ?
                                                             BigDecimal
                                                                     .valueOf(marketHandlerTimeMap.getOrDefault(marketMetricResult.getMarketId(), 0L))
                                                                     .divide(BigDecimal.valueOf(marketMetricResult.getMetricStruct()
                                                                                                        .getFinished()), 2, RoundingMode.HALF_UP)
                                                                     .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                                     .doubleValue() : 0)
                                                       .build());
            }
        }

        double avgHandlerTime = 0;
        if (0 != totalFinishNum) {
            avgHandlerTime = BigDecimal.valueOf(hiddendanger.getHandleTotalTime())
                    .divide(BigDecimal.valueOf(totalFinishNum), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        hiddendanger.setAvgHandlerTime(avgHandlerTime);
        hiddendanger.setTopMarketMetric(marketCompletedRateMetrics.stream().sorted(Comparator.comparing(MarketCompletedRateMetric::getRate))
                                                .collect(Collectors.toList()));
        hiddendanger.setLowMarketMetric(marketCompletedRateMetrics.stream()
                                                .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed())
                                                .collect(Collectors.toList()));
        return hiddendanger;
    }

    /**
     * 故障报修处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_FAULT_ORDER_COMPLETED_RATE_RANK_METRIC", description = "隐患整改处理率指标")
    public HeadWorkOrderCompletedRankMetricDTO headquartersFaultOrderRateRankMetric(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});

        Integer total = 0;
        Integer finished = 0;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<FaultCompletedMetricVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                total += marketMetricResult.getMetricStruct().getTotal();
                finished += marketMetricResult.getMetricStruct().getCompleted();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .number(Long.valueOf(marketMetricResult.getMetricStruct().getCompleted()))
                                                       .rate(marketMetricResult.getMetricStruct().getRate())
                                                       .build());
            }
        }
        HeadWorkOrderCompletedRankMetricDTO fault = getOrderCompletedRankMetricDTO(param, "FAULT");
        fault.setRepairCount(total);
        fault.setHandleCount(finished);
        fault.setHandleRate(total != 0 ? BigDecimal.valueOf(finished)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue() : 0);
        fault.setTopMarketMetric(marketCompletedRateMetrics.stream()
                                         .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed())
                                         .limit(5).collect(Collectors.toList()));
        fault.setLowMarketMetric(marketCompletedRateMetrics.stream().sorted(Comparator.comparing(MarketCompletedRateMetric::getRate))
                                         .limit(5).collect(Collectors.toList()));

        return fault;
    }

    /**
     * 故障报修处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_FAULT_ORDER_COMPLETED_EFFICIENCY_RANK_METRIC", description = "隐患整改处理效率指标")
    public HeadWorkOrderEfficiencyMetricDTO headquartersFaultOrderEfficiencyRankMetric(MetricParam param) {
        List<MarketMetricResult<FaultCompletedMetricVO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(FAULT_REPAIR_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<FaultCompletedMetricVO>() {});
        Map<Long, Long> marketHandlerTimeMap = new HashMap<>();
        HeadWorkOrderEfficiencyMetricDTO fault = getOrderEfficiencyMetricDTO(param, "FAULT", marketHandlerTimeMap);
        Integer totalFinishNum = 0;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<FaultCompletedMetricVO> marketMetricResult : marketMetricResultList) {
            if (Objects.nonNull(marketMetricResult.getMetricStruct())) {
                totalFinishNum += marketMetricResult.getMetricStruct().getTotal();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(marketMetricResult.getMarketName())
                                                       .number(Long.valueOf(marketMetricResult.getMetricStruct().getCompleted()))
                                                       .rate(0 != marketMetricResult.getMetricStruct().getCompleted() ?
                                                             BigDecimal
                                                                     .valueOf(marketHandlerTimeMap.getOrDefault(marketMetricResult.getMarketId(), 0L))
                                                                     .divide(BigDecimal.valueOf(marketMetricResult.getMetricStruct()
                                                                                                        .getCompleted()), 2, RoundingMode.HALF_UP)
                                                                     .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                                                                     .doubleValue() : 0)
                                                       .build());
            }
        }

        double avgHandlerTime = 0;
        if (0 != totalFinishNum) {
            avgHandlerTime = BigDecimal.valueOf(fault.getHandleTotalTime())
                    .divide(BigDecimal.valueOf(totalFinishNum), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        fault.setAvgHandlerTime(avgHandlerTime);
        return fault;
    }

    private HeadWorkOrderCompletedRankMetricDTO getOrderCompletedRankMetricDTO(MetricParam param, String businessType) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<MarketMetricResult<List<HiddendangerWordOrderStatisticsVO>>> hiddendangerWordOrderStatisticsVOS = asyncHttpClientUtils
                .getAllMarketMetrics(WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<List<HiddendangerWordOrderStatisticsVO>>() {});

        List<HandlerTypeRankMetricDTO> topHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
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
                    return HandlerTypeRankMetricDTO.builder()
                            .marketName(t.getMarketName())
                            .type(t.getHiddenTypeName())
                            .rate(rate)
                            .build();
                })
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<HandlerTypeRankMetricDTO> lowHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    for (HiddendangerWordOrderStatisticsVO statistics : t.getMetricStruct()) {
                        statistics.setMarketName(t.getMarketName());
                    }
                    return t.getMetricStruct();
                }).flatMap(Collection::stream)
                .map(t ->
                     {
                         double rate = 0;
                         if (0 != t.getTotalNumber()) {
                             rate = BigDecimal.valueOf(t.getDealNumber())
                                     .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                     .multiply(BigDecimal.valueOf(100))
                                     .doubleValue();
                         }
                         return HandlerTypeRankMetricDTO.builder()
                                 .marketName(t.getMarketName())
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     })
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
        return HeadWorkOrderCompletedRankMetricDTO.builder()
                .topHandlerTypeMetric(topHandlerRate)
                .lowHandlerTypeMetric(lowHandlerRate)
                .build();
    }

    private HeadWorkOrderEfficiencyMetricDTO getOrderEfficiencyMetricDTO(MetricParam param, String businessType,
                                                                         Map<Long, Long> marketHandlerTimeMap
                                                                        ) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<MarketMetricResult<List<HiddendangerWordOrderStatisticsVO>>> hiddendangerWordOrderStatisticsVOS = asyncHttpClientUtils
                .getAllMarketMetrics(WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<List<HiddendangerWordOrderStatisticsVO>>() {});

        Long totalHandlerTime = hiddendangerWordOrderStatisticsVOS.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(MarketMetricResult::getMetricStruct)
                .flatMap(Collection::stream)
                .map(HiddendangerWordOrderStatisticsVO::getTotalWaitTime)
                .reduce(Long::sum).orElse(0L);
        List<HandlerTypeRankMetricDTO> topHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    for (HiddendangerWordOrderStatisticsVO statisticsVO : t.getMetricStruct()) {
                        statisticsVO.setMarketName(t.getMarketName());
                    }
                    marketHandlerTimeMap.putIfAbsent(t.getMarketId(), t.getMetricStruct().stream()
                            .map(HiddendangerWordOrderStatisticsVO::getTotalWaitTime)
                            .reduce(Long::sum).orElse(0L));
                    return t.getMetricStruct();
                }).flatMap(Collection::stream)
                .map(t -> {
                         double rate = 0;
                         if (0 != t.getTotalNumber()) {
                             rate = BigDecimal.valueOf(t.getTotalWaitTime())
                                     .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                     .doubleValue();
                         }
                         return HandlerTypeRankMetricDTO.builder()
                                 .marketName(t.getMarketName())
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     }
                    )
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<HandlerTypeRankMetricDTO> lowHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    for (HiddendangerWordOrderStatisticsVO statisticsVO : t.getMetricStruct()) {
                        statisticsVO.setMarketName(t.getMarketName());
                    }
                    return t.getMetricStruct();
                }).flatMap(Collection::stream)
                .map(t -> {
                         double rate = 0;
                         if (0 != t.getTotalNumber()) {
                             rate = BigDecimal.valueOf(t.getTotalWaitTime())
                                     .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                     .doubleValue();
                         }
                         return HandlerTypeRankMetricDTO.builder()
                                 .marketName(t.getMarketName())
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     }
                    )
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
        return HeadWorkOrderEfficiencyMetricDTO.builder()
                .handleTotalTime(totalHandlerTime)
                .topHandlerTypeMetric(topHandlerRate)
                .lowHandlerTypeMetric(lowHandlerRate)
                .build();
    }

    /**
     * 电子巡更异常率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_ELECTRON_ERROR_RATE", description = "电子巡更异常率指标")
    public ElectronErrorRateMetricDTO electronErrRate(ElectronQueryParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronErrMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long errorNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketElectronErrMetric : allMarketElectronErrMetrics) {
            if (Objects.nonNull(allMarketElectronErrMetric.getMetricStruct())) {
                errorNum += allMarketElectronErrMetric.getMetricStruct().getNumber();
                totalNum += allMarketElectronErrMetric.getMetricStruct().getTotal();
            }
        }
        return ElectronErrorRateMetricDTO.builder().electronTotal(totalNum)
                .electronAbnormalNumber(errorNum)
                .electronAbnormalRate(BigDecimal.valueOf(errorNum)
                                              .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)).build();
    }

    /**
     * 电子巡更完成率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_ELECTRON_COMPLETE_RATE", description = "电子巡更完成率指标")
    public ElectronCompleteRateMetricDTO electronCompleteRate(ElectronQueryParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long completeNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketElectronCompleteMetric : allMarketElectronCompleteMetrics) {
            if (Objects.nonNull(allMarketElectronCompleteMetric.getMetricStruct())) {
                completeNum += allMarketElectronCompleteMetric.getMetricStruct().getNumber();
                totalNum += allMarketElectronCompleteMetric.getMetricStruct().getTotal();
            }
        }

        return ElectronCompleteRateMetricDTO.builder()
                .electronTotal(totalNum)
                .electronCompleteNumber(completeNum)
                .electronCompleteRate(BigDecimal.valueOf(completeNum)
                                              .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)).build();
    }

    /**
     * 电子巡更总数指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_ELECTRON_TASK_TOTAL", description = "电子巡更总数指标")
    public ElectronTotalMetricDTO electronTaskTotal(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronTotalMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_TOTAL_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        return ElectronTotalMetricDTO.builder().electronTotal(allMarketElectronTotalMetrics.stream()
                                                                      .filter(t -> Objects.nonNull(t.getMetricStruct()))
                                                                      .map(t -> t.getMetricStruct().getTotal())
                                                                      .reduce(Long::sum).orElse(0L))
                .build();
    }

    /**
     * 电子巡更处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_ELECTRON_HANDLE_RATE_RANK_METRIC", description = "电子巡更处理率指标")
    public HeadElectronHandlerRateRankMetricDTO headquartersElectronHandleRateRankMetric(ElectronMetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> marketElectronMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long completeNum = 0L;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<TotalNumberRateDTO> allMarketElectronCompleteMetric : marketElectronMetricResultList) {
            if (Objects.nonNull(allMarketElectronCompleteMetric.getMetricStruct())) {
                completeNum += allMarketElectronCompleteMetric.getMetricStruct().getNumber();
                totalNum += allMarketElectronCompleteMetric.getMetricStruct().getTotal();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(allMarketElectronCompleteMetric.getMarketName())
                                                       .number(allMarketElectronCompleteMetric.getMetricStruct().getNumber())
                                                       .rate(allMarketElectronCompleteMetric.getMetricStruct().getRate().doubleValue())
                                                       .build());
            }
        }
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});
        List<HandlerRateDTO> topHandlerDept = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerRateDept().forEach(user ->
                                                                                user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerRateDept();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5).collect(Collectors.toList());
        List<HandlerRateDTO> bottomHandlerDept = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerRateDept().forEach(user ->
                                                                                   user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerRateDept();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5).collect(Collectors.toList());

        return HeadElectronHandlerRateRankMetricDTO.builder()
                .patrolTaskTotal(totalNum)
                .patrolTaskHandlerNumber(completeNum)
                .patrolTaskHandledRate(0 != totalNum ? 0d : BigDecimal.valueOf(completeNum)
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP).doubleValue())
                .topHandlerRateMarket(marketCompletedRateMetrics.stream()
                                              .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed()).limit(5)
                                              .collect(Collectors.toList()))
                .bottomHandlerRateMarket(marketCompletedRateMetrics.stream()
                                                 .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate)).limit(5)
                                                 .collect(Collectors.toList()))
                .topHandlerRateDept(topHandlerDept)
                .bottomHandlerRateDept(bottomHandlerDept)
                .build();
    }

    /**
     * 电子巡更处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_ELECTRON_HANDLE_EFFICIENCY_RANK_METRIC", description = "电子巡更处理效率指标")
    public HeadElectronHandlerEfficiencyRankMetricDTO headquartersElectronHandleEfficiencyRankMetric(MetricParam param) {
        List<MarketMetricResult<TaskHandlerTimeVO>> marketTimeMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_NUMBER_TIME_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TaskHandlerTimeVO>() {});

        Long taskHandlerNumber = 0L;
        Long taskTotalTime = 0L;
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = new ArrayList<>();
        for (MarketMetricResult<TaskHandlerTimeVO> metricResult : marketTimeMetricResultList) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                taskHandlerNumber += metricResult.getMetricStruct().getTaskHandlerNumber();
                taskTotalTime += metricResult.getMetricStruct().getTaskTotalTime();

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
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        List<HandlerEfficiencyDTO> topHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerEfficiencyUser().forEach(user ->
                                                                                      user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5).collect(Collectors.toList());
        List<HandlerEfficiencyDTO> bottomHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerEfficiencyUser().forEach(user ->
                                                                                         user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5).collect(Collectors.toList());
        double avgHandlerTime = 0;
        if (0 != taskHandlerNumber) {
            avgHandlerTime = BigDecimal.valueOf(taskTotalTime)
                    .divide(BigDecimal.valueOf(taskHandlerNumber), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return HeadElectronHandlerEfficiencyRankMetricDTO.builder()
                .patrolTaskHandlerNumber(taskHandlerNumber)
                .patrolTaskTotalTime(taskTotalTime)
                .avgHandlerTime(avgHandlerTime)
                .topHandlerRateMarket(handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5)
                                              .collect(Collectors.toList()))
                .bottomHandlerRateMarket(handlerEfficiencyDTOS.stream()
                                                 .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5)
                                                 .collect(Collectors.toList()))
                .topHandlerRateUser(topHandlerUser)
                .bottomHandlerRateUser(bottomHandlerUser)
                .build();
    }

    /**
     * 日常巡检异常率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_ERROR_RATE", description = "日常巡检异常率指标")
    public InspectionErrorRateMetricDTO inspectionErrorRate(InspectionMetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionErrorMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long errorNum = 0L;
        List<MarketCompletedRateMetric> errorRateMarketList = new ArrayList<>();
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionErrorMetric : allMarketInspectionErrorMetrics) {
            if (Objects.nonNull(allMarketInspectionErrorMetric.getMetricStruct())) {
                errorNum += allMarketInspectionErrorMetric.getMetricStruct().getNumber();
                totalNum += allMarketInspectionErrorMetric.getMetricStruct().getTotal();
                errorRateMarketList.add(MarketCompletedRateMetric.builder()
                                                .marketName(allMarketInspectionErrorMetric.getMarketName())
                                                .number(allMarketInspectionErrorMetric.getMetricStruct().getNumber())
                                                .rate(allMarketInspectionErrorMetric.getMetricStruct().getRate().doubleValue())
                                                .build());
            }
        }
        return InspectionErrorRateMetricDTO.builder().inspectionTotal(totalNum)
                .inspectionAbnormalNumber(errorNum)
                .inspectionAbnormalRate(
                        totalNum == 0 ? BigDecimal.ZERO :
                        BigDecimal.valueOf(errorNum)
                                .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP))
                .topHandlerRateMarket(errorRateMarketList.stream()
                                              .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed()).limit(5)
                                              .collect(Collectors.toList()))
                .bottomHandlerRateMarket(errorRateMarketList.stream()
                                                 .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate)).limit(5)
                                                 .collect(Collectors.toList()))
                .build();
    }

    /**
     * 日常巡检完成率指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_COMPLETE_RATE", description = "日常巡检完成率指标")
    public InspectionCompleteRateMetricDTO inspectionCompleteRate(InspectionMetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long completeNum = 0L;
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionCompleteMetric : allMarketInspectionCompleteMetrics) {
            if (Objects.nonNull(allMarketInspectionCompleteMetric.getMetricStruct())) {
                completeNum += allMarketInspectionCompleteMetric.getMetricStruct().getNumber();
                totalNum += allMarketInspectionCompleteMetric.getMetricStruct().getTotal();
            }
        }

        return InspectionCompleteRateMetricDTO.builder().inspectTotal(totalNum)
                .inspectionCompleteNumber(completeNum)
                .inspectCompleteRate(BigDecimal.valueOf(completeNum)
                                             .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP)).build();
    }

    /**
     * 日常巡检总数指标
     *
     * @param param 时间条件
     *
     * @return 率
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_TASK_TOTAL", description = "日常巡检总数指标")
    public InspectionTotalMetricDTO inspectionTaskTotal(MetricParam param) {
        List<MarketMetricResult<Long>> allMarketInspectionTotalMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_TOTAL_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<Long>() {});
        return InspectionTotalMetricDTO.builder().inspectionTotal(allMarketInspectionTotalMetrics.stream()
                                                                          .filter(t -> Objects.nonNull(t.getMetricStruct()))
                                                                          .map(MarketMetricResult::getMetricStruct)
                                                                          .reduce(Long::sum).orElse(0L)).build();
    }

    /**
     * 日常巡检处理率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_HANDLE_RATE_RANK_METRIC", description = "日常巡检处理率指标")
    public HeadInspectionHandlerRateRankMetricDTO headquartersInspectionHandleRate(MetricParam param) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TotalNumberRateDTO>() {});
        Long totalNum = 0L;
        Long completeNum = 0L;
        List<MarketCompletedRateMetric> marketCompletedRateMetrics = new ArrayList<>();
        for (MarketMetricResult<TotalNumberRateDTO> allMarketInspectionCompleteMetric : allMarketInspectionCompleteMetrics) {
            if (Objects.nonNull(allMarketInspectionCompleteMetric.getMetricStruct())) {
                completeNum += allMarketInspectionCompleteMetric.getMetricStruct().getNumber();
                totalNum += allMarketInspectionCompleteMetric.getMetricStruct().getTotal();
                marketCompletedRateMetrics.add(MarketCompletedRateMetric.builder()
                                                       .marketName(allMarketInspectionCompleteMetric.getMarketName())
                                                       .number(allMarketInspectionCompleteMetric.getMetricStruct().getNumber())
                                                       .rate(allMarketInspectionCompleteMetric.getMetricStruct().getRate().doubleValue())
                                                       .build());
            }
        }
        List<MarketMetricResult<HandlerRateMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_RATE_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerRateMetricDTO>() {});
        List<HandlerRateDTO> topHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerRateUser().forEach(user ->
                                                                                user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerRateUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate).reversed()).limit(5).collect(Collectors.toList());
        List<HandlerRateDTO> bottomHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerRateUser().forEach(user ->
                                                                                   user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerRateUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerRateDTO::getRate)).limit(5).collect(Collectors.toList());
        return HeadInspectionHandlerRateRankMetricDTO.builder()
                .inspectTaskTotal(totalNum)
                .inspectTaskHandlerNumber(completeNum)
                .inspectTaskHandledRate(totalNum == 0L ? BigDecimal.ZERO : BigDecimal.valueOf(completeNum)
                        .divide(BigDecimal.valueOf(totalNum), 2, RoundingMode.HALF_UP))
                .topHandlerRateMarket(marketCompletedRateMetrics.stream()
                                              .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate).reversed()).limit(5)
                                              .collect(Collectors.toList()))
                .bottomHandlerRateMarket(marketCompletedRateMetrics.stream()
                                                 .sorted(Comparator.comparing(MarketCompletedRateMetric::getRate)).limit(5)
                                                 .collect(Collectors.toList()))
                .topHandlerRateUser(topHandlerUser)
                .bottomHandlerRateUser(bottomHandlerUser)
                .build();
    }

    /**
     * 日常巡检处理效率指标
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_HANDLE_EFFICIENCY_RANK_METRIC", description = "日常巡检处理效率指标")
    public HeadInspectionHandlerEfficiencyRankMetricDTO headquartersInspectionHandleEfficiencyRankMetric(MetricParam param) {

        List<MarketMetricResult<TaskHandlerTimeVO>> marketTimeMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<TaskHandlerTimeVO>() {});
        Long taskHandlerNumber = 0L;
        Long taskTotalTime = 0L;
        List<HandlerEfficiencyDTO> handlerEfficiencyDTOS = new ArrayList<>();
        for (MarketMetricResult<TaskHandlerTimeVO> metricResult : marketTimeMetricResultList) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                taskHandlerNumber += metricResult.getMetricStruct().getTaskHandlerNumber();
                taskTotalTime += metricResult.getMetricStruct().getTaskTotalTime();

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
        List<MarketMetricResult<HandlerEfficiencyMetricDTO>> marketMetricResultList = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<HandlerEfficiencyMetricDTO>() {});
        List<HandlerEfficiencyDTO> topHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopHandlerEfficiencyUser().forEach(user ->
                                                                                      user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5).collect(Collectors.toList());
        List<HandlerEfficiencyDTO> bottomHandlerUser = marketMetricResultList.stream().filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomHandlerEfficiencyUser().forEach(user ->
                                                                                         user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomHandlerEfficiencyUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5).collect(Collectors.toList());
        double avgHandlerTime = 0;
        if (0 != taskHandlerNumber) {
            avgHandlerTime = BigDecimal.valueOf(taskTotalTime)
                    .divide(BigDecimal.valueOf(taskHandlerNumber), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return HeadInspectionHandlerEfficiencyRankMetricDTO.builder()
                .inspectTaskHandlerNumber(taskHandlerNumber)
                .inspectTaskHandlerTime(taskTotalTime)
                .avgHandlerTime(avgHandlerTime)
                .topHandlerRateMarket(handlerEfficiencyDTOS.stream().sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime)).limit(5)
                                              .collect(Collectors.toList()))
                .bottomHandlerRateMarket(handlerEfficiencyDTOS.stream()
                                                 .sorted(Comparator.comparing(HandlerEfficiencyDTO::getAvgTime).reversed()).limit(5)
                                                 .collect(Collectors.toList()))
                .topHandlerRateUser(topHandlerUser)
                .bottomHandlerRateUser(bottomHandlerUser)
                .build();
    }

    /**
     * 日常巡检次数指标
     */
    @MetricTopic(topic = "HEADQUARTERS_INSPECT_RECORD_NUM_METRIC", description = "日常巡检次数指标")
    public HeadInspectionRecordNumMetricDTO headquartersInspectionRecordNumMetric(MetricParam param) {
        List<MarketMetricResult<InspectionRecordNumMetricDTO>> marketNumberMetricResults = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_RECORD_USER_NUMBER_REQUEST_URL, HttpMethod.GET, null, param, new TypeReference<InspectionRecordNumMetricDTO>() {});
        Long inspectNum = 0L;
        List<InspectionRecordUserNumVO> inspectionRecordUserNumVOS = new ArrayList<>();
        for (MarketMetricResult<InspectionRecordNumMetricDTO> metricResult : marketNumberMetricResults) {
            if (Objects.nonNull(metricResult.getMetricStruct())) {
                inspectNum += metricResult.getMetricStruct().getInspectionRecordNum();
                InspectionRecordUserNumVO inspectionRecord = new InspectionRecordUserNumVO();
                inspectionRecord.setMarketName(metricResult.getMarketName());
                inspectionRecord.setNum(metricResult.getMetricStruct().getInspectionRecordNum());
                inspectionRecordUserNumVOS.add(inspectionRecord);
            }
        }
        List<InspectionRecordUserNumVO> topHandlerUser = marketNumberMetricResults.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getTopInspectUser().forEach(user ->
                                                                            user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getTopInspectUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum)).limit(5).collect(Collectors.toList());
        List<InspectionRecordUserNumVO> bottomHandlerUser = marketNumberMetricResults.stream()
                .filter(t -> Objects.nonNull(t.getMetricStruct()))
                .map(t -> {
                    t.getMetricStruct().getBottomInspectUser().forEach(user ->
                                                                               user.setMarketName(t.getMarketName()));
                    return t.getMetricStruct().getBottomInspectUser();
                }).flatMap(Collection::stream)
                .sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum).reversed()).limit(5).collect(Collectors.toList());
        return HeadInspectionRecordNumMetricDTO.builder()
                .inspectionRecordNum(inspectNum)
                .topInspectMarket(inspectionRecordUserNumVOS.stream()
                                          .sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum).reversed()).limit(5)
                                          .collect(Collectors.toList()))
                .bottomInspectMarket(inspectionRecordUserNumVOS.stream().sorted(Comparator.comparing(InspectionRecordUserNumVO::getNum))
                                             .limit(5).collect(Collectors.toList()))
                .topInspectUser(topHandlerUser)
                .bottomInspectUser(bottomHandlerUser)
                .build();
    }

    public List<InspectionRateMetricDTO> getAllMetric(InspectionMetricParam queryParam) {
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionErrorMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<TotalNumberRateDTO>() {});
        Map<Long, MarketMetricResult<TotalNumberRateDTO>> marketMetricResultMap = allMarketInspectionErrorMetrics.stream()
                .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketInspectionCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(INSPECTION_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<TotalNumberRateDTO>() {});
        return allMarketInspectionCompleteMetrics.stream().map(completeRate -> {
            MarketMetricResult<TotalNumberRateDTO> marketMetricResult = marketMetricResultMap.get(completeRate.getMarketId());
            return InspectionRateMetricDTO.builder()
                    .marketName(completeRate.getMarketName())
                    .inspectionTotal(completeRate.getMetricStruct().getTotal())
                    .inspectionCompleteNumber(completeRate.getMetricStruct().getNumber())
                    .inspectCompleteRate(completeRate.getMetricStruct().getRate())
                    .inspectionAbnormalNumber(marketMetricResult.getMetricStruct().getNumber())
                    .inspectionAbnormalRate(marketMetricResult.getMetricStruct().getRate())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<PatrolTaskRateMetricDTO> getAllPatrolMetric(ElectronMetricParam param) {
        ElectronQueryParam queryParam = this.convertQueryParam(param);
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronCompleteMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_COMPLETE_RATE_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<TotalNumberRateDTO>() {});
        List<MarketMetricResult<TotalNumberRateDTO>> allMarketElectronErrMetrics = asyncHttpClientUtils
                .getAllMarketMetrics(PATROL_EXCEPTION_RATE_REQUEST_URL, HttpMethod.GET, null, queryParam, new TypeReference<TotalNumberRateDTO>() {});
        Map<Long, MarketMetricResult<TotalNumberRateDTO>> marketMetricResultMap = allMarketElectronErrMetrics.stream()
                .collect(Collectors.toMap(MarketMetricResult::getMarketId, Function.identity()));
        return allMarketElectronCompleteMetrics.stream().map(completeRate -> {
                                                                 MarketMetricResult<TotalNumberRateDTO> marketMetricResult = marketMetricResultMap.get(completeRate.getMarketId());
                                                                 return PatrolTaskRateMetricDTO.builder()
                                                                         .marketName(completeRate.getMarketName())
                                                                         .patrolTotal(completeRate.getMetricStruct().getTotal())
                                                                         .patrolCompleteNumber(completeRate.getMetricStruct().getNumber())
                                                                         .patrolCompleteRate(completeRate.getMetricStruct().getRate())
                                                                         .patrolAbnormalNumber(marketMetricResult.getMetricStruct().getNumber())
                                                                         .patrolAbnormalRate(marketMetricResult.getMetricStruct().getRate())
                                                                         .build();
                                                             }
                                                            ).collect(Collectors.toList());
    }

    private ElectronQueryParam convertQueryParam(ElectronMetricParam param) {
        ElectronQueryParam queryParam = new ElectronQueryParam();
        queryParam.setPeriod(param.getPeriod());
        queryParam.setPointName(param.getPointName());
        queryParam.setAreaName(param.getAreaName());
        return queryParam;

    }
}
