package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-20  15:32
 * @Description:
 * @Version: 1.0
 */
@Data
public class LimitTimeNotifyDTO {
    private Boolean checked;
    private List<ApprovalReminderDTO> approvalReminder;
}
