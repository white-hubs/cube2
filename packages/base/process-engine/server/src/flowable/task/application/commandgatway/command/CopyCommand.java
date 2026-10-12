package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import com.guhecloud.cube.base.flow.flowable.definition.domain.valueobject.CopyUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/9/1 18:20
 * @description
 */
@AllArgsConstructor
@Getter
public class CopyCommand {
    private String taskKey;
    private String taskName;
    private String processInstanceId;

    private Set<CopyUser> copyUsers;
}
