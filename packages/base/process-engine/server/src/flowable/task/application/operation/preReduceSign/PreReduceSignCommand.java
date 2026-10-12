package com.guhecloud.cube.base.flow.flowable.task.application.operation.preReduceSign;

import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Data
@AllArgsConstructor
public class PreReduceSignCommand extends TaskOperationCommand {
    @NotBlank
    private String processInstanceId;
    @NotBlank
    private String taskKey;
    @NotEmpty
    private Set<Long> userIds;
}
