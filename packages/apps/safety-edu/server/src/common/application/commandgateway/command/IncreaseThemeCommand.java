package com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 主题新增命令
 *
 * @author lanchun
 * @date 2023/4/18 11:18
 */
@Data
public class IncreaseThemeCommand {

    /**
     * 主题名称
     */
    @NotEmpty(message = "主题名称不能为空")
    @Size(max = 20, message = "主题名称不能大于20")
    private String name;

    /**
     * 主题描述
     */
    @Size(max = 200, message = "备注说明不能大于200")
    private String remark;
}
