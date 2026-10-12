package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-20  15:29
 * @Description:
 * @Version: 1.0
 */
@Data
public class LimitTimeApprovalConfigDTO {
    /**
     * 超时提醒
     */
    private LimitTimeNotifyDTO limitTimeNotify;
    /**
     * 限时审批
     */
    private LimitTimeApprovalDTO limitTimeApproval;
    /**
     * 自动审批，兼容旧配置
     */
    private AutoPassDTO autoPass;
    /**
     * 表单内容
     */
    private List<FormPermsDTO> formPerms;
}
