package com.guhecloud.cube.base.flow.flowable.task.application.operation.repeat;

import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class RepeatCommand extends TaskOperationCommand {
    private String processInstanceId;
    private String account;
    private String data;
    private String comment;
    private Map<String, Object> variables;
}
