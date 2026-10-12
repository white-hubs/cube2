package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 指定审批人
 */
@Data
public class AppointApproverCommand {
    @NotBlank
    private String processInstanceId;
    @NotBlank
    private String taskKey;
    @NotEmpty
    private List<Long> userIds;
}
