package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.StatRolePanelEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class StatRolePanelModifyParam {

    /**
     * 角色主键
     */
    @NotNull(message = "角色主键不能为空")
    @Min(value = 1, message = "角色主键不能小于1")
    private Long roleId;

    /**
     * 是否启用
     */
    @NotNull(message = "启用按钮不能为空")
    private Boolean enabled;

    /**
     * 面板类型
     */
    @NotEmpty(message = "面板类型不能为空")
    private List<StatRolePanelEnum> panelTypes;

}
