package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商品分类新增信息
 */
@Data
public class GoodsCategoryCreateParam {
    private Long id;

    private String code;
    /**
     * 商品分类名称
     */
    @NotNull(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 父类主键
     */
    private Long parentId;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
