package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 方法保存参数
 *
 * @author lanchun
 * @date 2021/12/24 10:06
 */
@Data
public class FunctionModifyParam {

    /**
     * 功能编号
     */
    @NotEmpty(message = "功能编号不能为空")
    @Size(max = 100, message = "功能编号不能大于100")
    private String code;

    /**
     * 菜单主键
     */
    @NotNull(message = "菜单主键不能为空")
    @Min(value = 1, message = "菜单主键不能小于1")
    private Long menuId;

    /**
     * 功能名称
     */
    @NotEmpty(message = "功能名称不能为空")
    @Size(max = 100, message = "功能名称不能大于100")
    private String name;

    /**
     * 功能备注
     */
    @Size(max = 200, message = "功能备注不能大于100")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = false;
}
