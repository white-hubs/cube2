package com.guhecloud.cube.biz.mdata.server.stall.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 档位分类更新信息
 */
@Data
public class StallCategoryUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    @Size(max = 20, message = "名称不能大于20")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 文件
     */
    @Min(value = 1, message = "文件主键不能小于1")
    private Long fileId;
}
