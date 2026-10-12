package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Data
public class HiddenDangerTypeCommand {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 隐患分类主键
     */
    @NotNull(message = "隐患分类主键")
    private Long hiddenDangerCategoryId;

    /**
     * 隐患分类名称
     */
    @NotEmpty(message = "隐患分类名称不能为空")
    private String hiddenDangerCategoryName;

}
