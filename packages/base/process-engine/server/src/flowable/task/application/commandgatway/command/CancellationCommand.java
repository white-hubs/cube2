package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author 剑秋
 * @description
 * @date 2022/9/8
 * 作废
 * 数据需要回滚
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancellationCommand {
    private String processInstanceId;
    private String account;
    private String comment;

}
