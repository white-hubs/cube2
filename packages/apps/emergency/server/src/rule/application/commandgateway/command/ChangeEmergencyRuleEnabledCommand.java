package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 修改预案状态命令
 *
 * @author lanchun
 * @date 2022/9/5 10:39
 */
@Data
public class ChangeEmergencyRuleEnabledCommand {

    /**
     * 预案规则主键
     */
    @NotNull(message = "预案规则主键不能为空")
    private Long id;

    /**
     * 启用状态
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

    /**
     * 禁用时长(默认小时,暂不扩展其它方式)
     */
    private Integer disableDuration;
}
