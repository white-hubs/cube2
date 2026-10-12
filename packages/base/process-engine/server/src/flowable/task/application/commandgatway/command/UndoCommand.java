package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author 剑秋
 * @description 撤销命令
 * @date 2022/9/16
 *
 * 流程结束
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UndoCommand {
    private String account;
    private String processInstanceId;
    private String comment;
}
