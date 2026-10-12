package com.guhecloud.cube.base.flow.client.doamin.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-22  10:47
 * @Description: 审批类型
 * @Version: 1.0
 */
public enum ApprovalTypeEnum {
    /**
     * 人工审批
     */
    ARTIFICIAL("人工审批"),
    /**
     * 审批人为空自动审批通过
     */
    NO_APPROVER("审批人为空自动审批通过"),
    /**
     * 超时自动审批通过
     */
    OVERDUE_APPROVER("超时自动审批通过"),
    /**
     * 超时自动转交
     */
    OVERDUE_TRANSFER("超时自动转交");
    /**
     * 描述信息
     */
    private final String description;

    ApprovalTypeEnum(String description) {
        this.description = description;
    }
}
