package com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HandlerTypeRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerCompletedRankMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerEfficiencyMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto.HiddenDangerNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.client.HiddenDangerClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenWorkOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddendangerWordOrderStatisticsVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 隐患工单指标服务
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 11:56
 * @Description:
 */

@Service
public class HiddenDangerOrderMetricService {

    private final HiddenDangerClient hiddenDangerClient;

    public HiddenDangerOrderMetricService(HiddenDangerClient hiddenDangerClient) {
        this.hiddenDangerClient = hiddenDangerClient;
    }

    @MetricTopic(topic = "HIDDEN_DANGER_NUMBER", description = "隐患整改总数指标")
    public HiddenDangerNumMetricDTO hiddenDangerNum(MetricParam param) {
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        if (Objects.isNull(hiddenDangerNumStatisticsVO)) {
            return HiddenDangerNumMetricDTO.builder()
                    .hiddenDangerNumber(0)
                    .build();
        }
        return HiddenDangerNumMetricDTO.builder()
                .hiddenDangerNumber(hiddenDangerNumStatisticsVO.getTotal())
                .build();
    }

    /**
     * 隐患整改指标
     */
    @MetricTopic(topic = "HIDDEN_DANGER_COMPLETED_RATE", description = "隐患整改完成率指标")
    public HiddenDangerCompletedRateMetricDTO hiddenDangerCompletedRate(MetricParam param) {
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        if (Objects.isNull(hiddenDangerNumStatisticsVO)) {
            return HiddenDangerCompletedRateMetricDTO.builder()
                    .hiddenDangerCompletedNumber(0)
                    .hiddenDangerCompletedRate(0d)
                    .build();
        }
        return HiddenDangerCompletedRateMetricDTO.builder()
                .hiddenDangerCompletedNumber(hiddenDangerNumStatisticsVO.getFinished())
                .hiddenDangerCompletedRate(hiddenDangerNumStatisticsVO.getCalculateProportion())
                .build();
    }

    /**
     * 隐患整改处理率指标
     */
    @MetricTopic(topic = "HIDDEN_DANGER_DEAL_RATE_RANK_METRIC", description = "隐患整改处理率指标")
    public HiddenDangerCompletedRankMetricDTO hiddenDangerDealRate(MetricParam param) {
        return getOrderCompletedRankMetricDTO(param, "HIDDENDANGER");
    }

    /**
     * 隐患整改处理效率指标
     */
    @MetricTopic(topic = "HIDDEN_DANGER_DEAL_EFFICIENCY_RANK_METRIC", description = "隐患整改处理效率指标")
    public HiddenDangerEfficiencyMetricDTO hiddenDangerDealEfficiencyRankMetric(MetricParam param) {
        return getOrderEfficiencyMetricDTO(param, "HIDDENDANGER");
    }

    /**
     * 故障报修处理率指标
     */
    @MetricTopic(topic = "FAULT_ORDER_COMPLETED_RATE_RANK_METRIC", description = "隐患整改处理率指标")
    public HiddenDangerCompletedRankMetricDTO faultOrderRateRankMetric(MetricParam param) {
        return getOrderCompletedRankMetricDTO(param, "FAULT");
    }

    /**
     * 故障报修处理效率指标
     */
    @MetricTopic(topic = "FAULT_ORDER_COMPLETED_EFFICIENCY_RANK_METRIC", description = "隐患整改处理效率指标")
    public HiddenDangerEfficiencyMetricDTO faultOrderEfficiencyRankMetric(MetricParam param) {
        return getOrderEfficiencyMetricDTO(param, "FAULT");
    }

    private HiddenDangerCompletedRankMetricDTO getOrderCompletedRankMetricDTO(MetricParam param, String businessType) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<HiddendangerWordOrderStatisticsVO> hiddendangerWordOrderStatisticsVOS =
                hiddenDangerClient.hiddendangerWorkOrderStatisticsAll(queryParm);
        List<HandlerTypeRankMetricDTO> topHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .map(t -> {
                    double rate = 0;
                    if (0 != t.getTotalNumber()) {
                        rate = BigDecimal.valueOf(t.getDealNumber())
                                .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue();
                    }
                    return HandlerTypeRankMetricDTO.builder()
                            .type(t.getHiddenTypeName())
                            .rate(rate)
                            .build();
                })
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<HandlerTypeRankMetricDTO> lowHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
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
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     })
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());

        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        return HiddenDangerCompletedRankMetricDTO.builder()
                .repairCount(hiddenDangerNumStatisticsVO.getTotal())
                .handleCount(hiddenDangerNumStatisticsVO.getFinished())
                .handleRate(hiddenDangerNumStatisticsVO.getCalculateProportion())
                .topHandlerTypeMetric(topHandlerRate)
                .lowHandlerTypeMetric(lowHandlerRate)
                .build();
    }

    private HiddenDangerEfficiencyMetricDTO getOrderEfficiencyMetricDTO(MetricParam param, String businessType) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<HiddendangerWordOrderStatisticsVO> hiddendangerWordOrderStatisticsVOS =
                hiddenDangerClient.hiddendangerWorkOrderStatisticsAll(queryParm);
        Long totalHandlerTime = hiddendangerWordOrderStatisticsVOS.stream().map(HiddendangerWordOrderStatisticsVO::getTotalWaitTime)
                .reduce(Long::sum).orElse(0L);
        List<HandlerTypeRankMetricDTO> topHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .map(t -> {
                         double rate = 0;
                         if (0 != t.getTotalNumber()) {
                             rate = BigDecimal.valueOf(t.getTotalWaitTime())
                                     .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                     .doubleValue();
                         }
                         return HandlerTypeRankMetricDTO.builder()
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     }
                    )
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        List<HandlerTypeRankMetricDTO> lowHandlerRate = hiddendangerWordOrderStatisticsVOS.stream()
                .map(t -> {
                         double rate = 0;
                         if (0 != t.getTotalNumber()) {
                             rate = BigDecimal.valueOf(t.getTotalWaitTime())
                                     .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                     .doubleValue();
                         }
                         return HandlerTypeRankMetricDTO.builder()
                                 .type(t.getHiddenTypeName())
                                 .rate(rate)
                                 .build();
                     }
                    )
                .sorted(Comparator.comparing(HandlerTypeRankMetricDTO::getRate))
                .limit(5)
                .collect(Collectors.toList());
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        double avgHandlerTime = 0;
        if (Objects.nonNull(hiddenDangerNumStatisticsVO) && 0 != hiddenDangerNumStatisticsVO.getFinished()) {
            avgHandlerTime = BigDecimal.valueOf(totalHandlerTime)
                    .divide(BigDecimal.valueOf(hiddenDangerNumStatisticsVO.getFinished()), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return HiddenDangerEfficiencyMetricDTO.builder()
                .handleCount(hiddenDangerNumStatisticsVO.getFinished())
                .handleTotalTime(totalHandlerTime)
                .avgHandlerTime(avgHandlerTime)
                .topHandlerTypeMetric(topHandlerRate)
                .lowHandlerTypeMetric(lowHandlerRate)
                .build();
    }

}
