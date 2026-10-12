package com.guhecloud.cube.base.flow.flowable.definition.domain;

import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalCommentConfig;
import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.ApprovalWay;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/30 9:31
 * @description 审批节点
 */
@Data
public class NewApprovalNode extends UserTaskNode {
    /**
     * 审批人--采用自定义EL表达式
     */
    private String approvalUserExpression;

    /**
     * 表单操作权限  -- json格式
     */
    private String data;

    /**
     * 审批方式
     */
    private ApprovalWay approvalWay;

    /**
     * 审批意见
     */
    private ApprovalCommentConfig approvalCommentConfig;

    /**
     * // 一个人完成审批，整个任务就算通过
     * ${nrOfCompletedInstances==1}
     *
     * // 所有人完成审批，整个任务才算过
     * ${nrOfCompletedInstances==nrOfInstances}
     *
     * // 一半人以上完成审批整个任务才算通过
     * ${nrOfCompletedInstances/nrOfInstances > 0.5}
     */
}
