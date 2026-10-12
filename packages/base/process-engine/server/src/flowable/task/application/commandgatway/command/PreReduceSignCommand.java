package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * 减签
 */
@Data
public class PreReduceSignCommand {
    @NotBlank
    private String processInstanceId;
    @NotBlank
    private String taskKey;
    @NotEmpty
    private Set<Long> userIds;
}
