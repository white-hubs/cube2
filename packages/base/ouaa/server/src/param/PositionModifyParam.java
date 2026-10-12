package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 岗位更新参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class PositionModifyParam {
    /**
     * 岗位名称
     */
    @NotEmpty(message = "职位名称不能为空")
    @Size(max = 100, message = "职位名称长度不能大于100")
    private String name;

    /**
     * 排序数字
     */
    @NotNull(message = "排序数字不能为空")
    @Min(value = 0, message = "排序数字不能小于0")
    private Float sort = 0F;

    /**
     * 岗位备注
     */
    @Size(max = 200, message = "职位备注长度不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled = true;
}
