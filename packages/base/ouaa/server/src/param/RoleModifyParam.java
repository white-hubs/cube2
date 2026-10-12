package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色更新参数
 *
 * @author Woody
 * @date 2021/7/17
 */
@Data
public class RoleModifyParam {
    /**
     * 产品主键
     */
    @Deprecated
    private Long productId;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能大于100")
    private String name;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注长度不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;
}
