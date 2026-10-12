package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.Data;

import java.util.Map;

/**
 * @author pengjian
 * @date 2022/9/7 20:12
 * @description 再次发起
 */
@Data
public class RepeatCommand {
    private String processInstanceId;
    private String account;
    private String data;
    private String comment;
    private Map<String, Object> variables;
}
