package com.guhecloud.cube.biz.metric.server.metric.result.service.fault;

import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.MetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto.FaultOrderCompletedRateMetricDTO;
import com.guhecloud.cube.biz.metric.server.metric.result.service.fault.dto.FaultOrderNumberMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.client.FaultClient;
import com.guhecloud.cube.srv.gen.maintain.vo.FaultCompletedMetricVO;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 故障工单指标服务
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 11:55
 * @Description:
 */

@Service
public class FaultOrderMetricService {

    private final FaultClient faultClient;

    public FaultOrderMetricService(FaultClient faultClient) {
        this.faultClient = faultClient;
    }

    @MetricTopic(topic = "FAULT_ORDER_NUMBER", description = "故障报修总数指标")
    public FaultOrderNumberMetricDTO faultNumberMetic(MetricParam param) {
        FaultCompletedMetricVO faultCompletedMetricVO = faultClient.faultCompletedMetic(param.getPeriod(), null);
        if (Objects.isNull(faultCompletedMetricVO)) {
            return FaultOrderNumberMetricDTO.builder()
                    .faultRepairNumber(0)
                    .build();
        }
        return FaultOrderNumberMetricDTO.builder()
                .faultRepairNumber(faultCompletedMetricVO.getTotal())
                .build();
    }

    /**
     * 故障保修指标
     */
    @MetricTopic(topic = "FAULT_ORDER_COMPLETED_RATE", description = "故障报修完成率指标")
    public FaultOrderCompletedRateMetricDTO faultOrderCompletedRate(MetricParam param) {
        FaultCompletedMetricVO faultCompletedMetricVO = faultClient.faultCompletedMetic(param.getPeriod(), null);
        if (Objects.isNull(faultCompletedMetricVO)) {
            return FaultOrderCompletedRateMetricDTO.builder()
                    .faultRepairCompletedNumber(0)
                    .faultRepairCompletedRate(0d)
                    .build();
        }
        return FaultOrderCompletedRateMetricDTO.builder()
                .faultRepairCompletedNumber(faultCompletedMetricVO.getCompleted())
                .faultRepairCompletedRate(faultCompletedMetricVO.getRate())
                .build();
    }
}
