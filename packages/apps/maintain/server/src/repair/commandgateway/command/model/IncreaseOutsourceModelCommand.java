package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.model;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/5/18 15:19
 * @description
 */
@Data
public class IncreaseOutsourceModelCommand {
    @NotBlank(message = "名称不能为空")
    private String name;
}
