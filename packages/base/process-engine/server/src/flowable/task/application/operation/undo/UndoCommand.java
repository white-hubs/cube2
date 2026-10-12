package com.guhecloud.cube.base.flow.flowable.task.application.operation.undo;

import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UndoCommand extends TaskOperationCommand {
    private String account;
    private String processInstanceId;
    private String comment;
}
