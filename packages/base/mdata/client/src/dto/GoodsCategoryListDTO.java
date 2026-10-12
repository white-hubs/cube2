package com.guhecloud.cube.biz.mdata.client.dto;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

@Data
public class GoodsCategoryListDTO extends Tree.Simple<Long, GoodsCategoryListDTO> {
    /**
     * 商品分类编码
     */
    private String code;

    /**
     * 商品分类名称
     */
    private String name;
}
