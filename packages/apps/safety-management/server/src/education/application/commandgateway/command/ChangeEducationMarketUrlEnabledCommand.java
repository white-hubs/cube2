package com.guhecloud.cube.srv.gen.safety.management.education.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 修改链接状态参数
 *
 * @author lanchun
 * @date 2024/4/11 10:17
 */
@Data
public class ChangeEducationMarketUrlEnabledCommand {

    /**
     * 链接主键
     */
    private Long id;

    /**
     * 启用状态
     */
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;
}
