package com.guhecloud.cube.biz.todo.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 待办标签保存参数
 *
 * @author lanchun
 * @date 2023/2/27 11:38
 */
@Data
public class TodoTagModifyParam {

    /**
     * 标签编号
     */
    @NotNull(message = "编号不能为空")
    @Size(max = 50, message = "编号不能大于名称个字符")
    private String code;

    /**
     * 标签名称
     */
    @NotNull(message = "名称不能为空")
    @Size(max = 50, message = "名称不能大于20个字符")
    private String name;

}
