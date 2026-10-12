package com.guhecloud.cube.base.flow.flowable.task.application.operation.reject;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.context.enumeration.RejectStrategyEnum;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectCommand extends TaskOperationCommand implements ApprovalOpinionSupport {
    private String taskId;
    private String account;

    private String comment;

    private String data;

    /**
     * 驳回策略
     */
    private RejectStrategyEnum rejectStrategyEnum;

    /**
     * 选择节点
     */
    private String selectNode;

    /**
     * 是否终止流程
     */
    private Boolean terminate;
    private ApprovalOpinion approvalOpinion;
}
