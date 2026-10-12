package com.guhecloud.cube.biz.daily.task.record.result;

import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import lombok.Data;

@Data
public class DailyRecordWorkOrderResult {
    private Long id;
    private Long taskId;
    private Long recordId;
    private Long targetId;
    private String targetCategory;
    /**
     * 工单类型(DailyWorkOrderTypeEnum)
     */
    private String workOrderType;
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    /**
     * 工单名称
     */
    private String processInstanceName;
    /**
     * 工单状态(FlowResult)
     */
    private String processInstanceStatus;

    public static DailyRecordWorkOrderResult of(DailyRecordWorkOrderPO po) {
        DailyRecordWorkOrderResult result = new DailyRecordWorkOrderResult();
        result.setId(po.getId());
        result.setTaskId(po.getTaskId());
        result.setRecordId(po.getRecordId());
        result.setTargetId(po.getTargetId());
        result.setTargetCategory(po.getTargetCategory());
        result.setWorkOrderType(po.getWorkOrderType());
        result.setProcessInstanceId(po.getProcessInstanceId());
        result.setProcessInstanceName(po.getProcessInstanceName());
        result.setProcessInstanceStatus(po.getProcessInstanceStatus());
        return result;
    }
}
