package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Data
public class HiddenDangerCategoryCommand {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;

}
