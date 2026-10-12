package com.guhecloud.cube.biz.metric.server.metric.result.service.flow;

import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.WorkOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.client.param.WordOrderStatisticsVO;
import com.guhecloud.cube.biz.metric.server.metric.result.base.annotation.MetricTopic;
import com.guhecloud.cube.biz.metric.server.metric.result.param.business.ProcessEngineMetricParam;
import com.guhecloud.cube.biz.metric.server.metric.result.service.flow.dto.WorkOrderNumberMetricDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessEngineMetricService {

    private final ProcessInstanceClient processInstanceClient;

    public ProcessEngineMetricService(ProcessInstanceClient processInstanceClient) {this.processInstanceClient = processInstanceClient;}

    /**
     * 工单类型数量指标
     *
     * @param param
     *
     * @return
     */
    @MetricTopic(topic = "PROCESS_ENGINE_WORK_ORDER_NUMBER_METRIC", description = "工单类型数量指标")
    public List<WorkOrderNumberMetricDTO> getWorkOrderTypeNumberMetric(ProcessEngineMetricParam param) {
        WordOrderStatisticsVO query = new WordOrderStatisticsVO();
        query.setPeriod(param.getPeriod());
        query.setBusinessTypes(String.join(",", param.getBusinessTypes()));
        List<WorkOrderStatisticsDTO> workOrderStatisticsDTOS = processInstanceClient.getNumberGroupByBusinessTypeTotal(query);
        return workOrderStatisticsDTOS.stream()
                .map(type ->
                             WorkOrderNumberMetricDTO.builder()
                                     .processNumber(type.getProcessNumber())
                                     .businessTypeName(type.getBusinessTypeName())
                                     .businessType(type.getBusinessType())
                                     .rejectNumber(type.getRejectNumber())
                                     .totalNumber(type.getTotalNumber())
                                     .undoNumber(type.getUndoNumber())
                                     .dealNumber(type.getDealNumber())
                                     .build()
                    ).collect(Collectors.toList());
    }
}
