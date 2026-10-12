package com.guhecloud.cube.biz.mdata.server.goods.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商品分类更新信息
 */
@Data
public class GoodsCategoryUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 商品分类名称
     */
    @NotNull(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

}
