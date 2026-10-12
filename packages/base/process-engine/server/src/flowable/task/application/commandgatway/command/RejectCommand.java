package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinion;
import com.guhecloud.cube.base.flow.client.doamin.ApprovalOpinionSupport;
import com.guhecloud.cube.base.flow.context.enumeration.RejectStrategyEnum;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/9/27 16:06
 * @description
 */
@Getter
@Setter
public class RejectCommand implements ApprovalOpinionSupport {
    @NotBlank
    private String taskId;
    private String account;
    private String data;
    private String comment;
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
    private ApprovalOpinion approvalOpinion = new ApprovalOpinion();
}
