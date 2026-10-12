package com.guhecloud.cube.base.flow.flowable.task.application.operation.preAddSign;

import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
@AllArgsConstructor
public class PreAddSignCommand extends TaskOperationCommand {
    @NotBlank
    private String processInstanceId;
    @NotBlank
    private String taskKey;
    @NotEmpty
    private List<Long> userIds;
}
