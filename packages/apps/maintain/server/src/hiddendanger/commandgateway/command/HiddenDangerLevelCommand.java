package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 隐患程度命令
 *
 * @author lzy
 * @date 2022/4/24
 */
@Data
public class HiddenDangerLevelCommand {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 20, message = "名称不能大于20")
    private String name;

}
