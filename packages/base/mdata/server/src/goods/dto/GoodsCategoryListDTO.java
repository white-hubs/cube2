package com.guhecloud.cube.biz.mdata.server.goods.dto;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
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
