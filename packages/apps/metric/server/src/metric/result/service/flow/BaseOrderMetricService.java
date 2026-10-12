package com.guhecloud.cube.biz.metric.server.metric.result.service.flow;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.client.HiddenDangerClient;
import com.guhecloud.cube.srv.gen.maintain.dto.HandlerRateDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.HiddenWorkOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.HiddendangerWordOrderStatisticsVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/15 19:35
 * @Description:
 */

@Service
public class BaseOrderMetricService {
    private final HiddenDangerClient hiddenDangerClient;
    private final FaultClient faultClient;

    public BaseOrderMetricService(HiddenDangerClient hiddenDangerClient, FaultClient faultClient) {
        this.hiddenDangerClient = hiddenDangerClient;
        this.faultClient = faultClient;
    }

    /**
     * 隐患整改总数指标
     */
    @MetricTopic(topic = "BASE_HIDDEN_DANGER_NUMBER", description = "隐患整改总数")
    public Integer hiddenDangerNum(MetricParam param) {
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        if (Objects.isNull(hiddenDangerNumStatisticsVO)) {
            return 0;
        }
        return hiddenDangerNumStatisticsVO.getTotal();
    }

    /**
     * 隐患整改数指标
     */
    @MetricTopic(topic = "BASE_HIDDEN_DANGER_COMPLETED_RATE", description = "隐患整改完成数")
    public Integer hiddenDangerCompletedRate(MetricParam param) {
        HiddenDangerNumStatisticsVO hiddenDangerNumStatisticsVO = hiddenDangerClient.hiddenDangerNumStatistics(param.getPeriod());
        if (Objects.isNull(hiddenDangerNumStatisticsVO)) {
            return 0;
        }
        return hiddenDangerNumStatisticsVO.getFinished();
    }

    /**
     * 隐患整改处理率最高的隐患类型
     */
    @MetricTopic(topic = "BASE_HIDDEN_DANGER_COMPLETED_RATE_RANK_METRIC", description = "隐患整改处理率最高的隐患类型")
    public List<HandlerRateDTO> hiddenDangerCompletedRateRankMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "HIDDENDANGER", Comparator.comparing(HandlerRateDTO::getRate).reversed());
    }

    private List<HandlerRateDTO> getHandlerRateDTOS(MetricParam param, String businessType,
                                                    Comparator<HandlerRateDTO> reversed
                                                   ) {
        HiddenWorkOrderStatisticsDTO queryParm = new HiddenWorkOrderStatisticsDTO();
        queryParm.setPeriod(param.getPeriod());
        queryParm.setApplicationCode(businessType);
        List<HiddendangerWordOrderStatisticsVO> hiddendangerWordOrderStatisticsVOS =
                hiddenDangerClient.hiddendangerWorkOrderStatisticsAll(queryParm);
        if (CollectionUtils.isEmpty(hiddendangerWordOrderStatisticsVOS)) {
            return CollectionUtils.emptyList();
        }
        return hiddendangerWordOrderStatisticsVOS.stream()
                .map(t -> {
                    double rate = 0;
                    if (0 != t.getTotalNumber()) {
                        rate = BigDecimal.valueOf(t.getDealNumber())
                                .divide(BigDecimal.valueOf(t.getTotalNumber()), 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .doubleValue();
                    }
                    return HandlerRateDTO.builder()
                            .name(t.getHiddenTypeName())
                            .num(Long.valueOf(t.getDealNumber()))
                            .rate(rate)
                            .build();
                })
                .sorted(reversed)
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 隐患整改处理率最低的隐患类型
     */

    @MetricTopic(topic = "BASE_HIDDEN_DANGER_COMPLETED_RATE_BOTTOM_METRIC", description = "隐患整改处理率最低的隐患类型")
    public List<HandlerRateDTO> hiddenDangerCompletedRateBottomMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "HIDDENDANGER", Comparator.comparing(HandlerRateDTO::getRate));
    }

    /**
     * 故障报修总数
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "BASE_FAULT_ORDER_NUMBER", description = "故障报修总数")
    public Integer faultNumberMetic(MetricParam param) {
        FaultCompletedMetricVO faultCompletedMetricVO = faultClient.faultCompletedMetic(param.getPeriod(), null);
        if (Objects.isNull(faultCompletedMetricVO)) {
            return 0;
        }
        return faultCompletedMetricVO.getTotal();
    }

    /**
     * 故障报修完成数
     */
    @MetricTopic(topic = "BASE_FAULT_ORDER_COMPLETED_RATE", description = "故障报修完成数")
    public Integer faultOrderCompletedRate(MetricParam param) {
        FaultCompletedMetricVO faultCompletedMetricVO = faultClient.faultCompletedMetic(param.getPeriod(), null);
        if (Objects.isNull(faultCompletedMetricVO)) {
            return 0;
        }
        return faultCompletedMetricVO.getCompleted();
    }

    /**
     * 故障报修处理率最高的故障类型
     */
    @MetricTopic(topic = "BASE_FAULT_ORDER_COMPLETED_RATE_RANK_METRIC", description = "故障报修处理率最高的故障类型")
    public List<HandlerRateDTO> faultOrderRateRankMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "FAULT", Comparator.comparing(HandlerRateDTO::getRate).reversed());
    }

    /**
     * 故障报修处理率最低的故障类型
     */
    @MetricTopic(topic = "BASE_FAULT_ORDER_COMPLETED_RATE_BOTTOM_METRIC", description = "故障报修处理率最低的故障类型")
    public List<HandlerRateDTO> faultOrderRateBottomMetric(MetricParam param) {
        return getHandlerRateDTOS(param, "FAULT", Comparator.comparing(HandlerRateDTO::getRate));

    }
}
