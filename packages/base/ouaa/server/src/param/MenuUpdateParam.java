package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 菜单修改参数
 *
 * @author lanchun
 * @date 2021/10/13 19:54
 */
@Data
public class MenuUpdateParam {

    /**
     * 分类字典主键
     */
    @Min(value = 1, message = "分类字典主键不能小于1")
    private Long categoryId;

    /**
     * 菜单编号
     */
    @NotEmpty(message = "菜单编号不能为空")
    @Size(max = 100, message = "菜单编号不能大于100")
    private String code;

    /**
     * 菜单名称
     */
    @NotEmpty(message = "菜单名称不能为空")
    @Size(max = 100, message = "菜单名称不能大于100")
    private String name;

    /**
     * 菜单图标
     */
    @Size(max = 100, message = "菜单图标不能大于100")
    private String icon;

    /**
     * 菜单路径
     */
    @Size(max = 100, message = "菜单路径不能大于100")
    private String path;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 菜单备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;
}
