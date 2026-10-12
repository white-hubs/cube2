package com.guhecloud.cube.biz.daily.task.record.param;

import com.guhecloud.cube.biz.daily.task.domain.listener.adapter.FlowResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DailyLinkWorkOrderParam {
    private Long taskId;
    @NotNull(message = "记录不能为空")
    private Long recordId;
    private Long targetId;
    private String workOrderType;
    @NotBlank(message = "流程实例不能为空")
    private String processInstanceId;
    private String processInstanceName;

    public DailyRecordWorkOrderPO toDailyRecordWorkOrderPO() {
        DailyRecordWorkOrderPO po = new DailyRecordWorkOrderPO();
        po.setTaskId(this.taskId);
        po.setRecordId(this.recordId);
        po.setTargetId(this.targetId);
        po.setWorkOrderType(this.workOrderType);
        po.setProcessInstanceId(this.processInstanceId);
        po.setProcessInstanceName(this.processInstanceName);
        po.setProcessInstanceStatus(FlowResult.ACTIVE.name());
        return po;
    }
}
