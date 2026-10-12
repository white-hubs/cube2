package com.guhecloud.cube.base.flow.flowable.task.application.operation.copy;

import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.CopyUser;
import com.guhecloud.cube.base.flow.flowable.task.application.operation.TaskOperationCommand;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CopyCommand extends TaskOperationCommand {
    private String taskKey;
    private String taskName;
    private String processInstanceId;

    private Set<CopyUser> copyUsers;
}
