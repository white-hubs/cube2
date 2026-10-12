package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.follow;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Data
public class IncreaseFollowUpCommand {

    /**
     * 报警记录主键
     */
    @NotNull(message = "报警记录主键不能为空")
    private Long id;

    /**
     * 跟进记录信息
     */
    @NotNull(message = "跟进记录信息不能为空")
    @Size(max = 500, message = "跟进记录信息不能大于500")
    private String content;

    /**
     * 文件主键集合
     */
    private Set<Long> files;
}
