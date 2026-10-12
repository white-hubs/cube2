package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

/**
 * 限时审批配置。
 */
@Data
public class LimitTimeApprovalDTO {
    /**
     * 是否开启
     */
    private Boolean checked;
    /**
     * 单位
     */
    private String unit;
    /**
     * 时间
     */
    private Double value;
    /**
     * 超时处理方式：PASS 自动通过，TRANSFER 转交。
     */
    private String type;
    /**
     * 转交目标审批人，多个用户使用逗号分隔。
     */
    private String targetAccount;
}
