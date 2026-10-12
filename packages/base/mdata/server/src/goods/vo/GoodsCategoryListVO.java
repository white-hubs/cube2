package com.guhecloud.cube.biz.mdata.server.goods.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsCategoryListVO extends Tree.Simple<Long, GoodsCategoryListVO> {

    /**
     * 商品分类编码
     */
    private String code;

    /**
     * 商品分类名称
     */
    private String name;
}
