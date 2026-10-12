package com.guhecloud.cube.biz.mdata.server.goods.converters;

import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.vo.GoodsCategoryListVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class GoodsCategoryConverterFoVO {
    private GoodsCategoryConverterFoVO() {

    }

    public static GoodsCategoryListVO toVO(GoodsCategory goodsCategory) {
        var goodsCategoryListVO = new GoodsCategoryListVO();
        goodsCategoryListVO.setId(goodsCategory.getId());
        goodsCategoryListVO.setCode(goodsCategory.getCode());
        goodsCategoryListVO.setName(goodsCategory.getName());
        goodsCategoryListVO.setParentId(goodsCategory.getParentId());
        goodsCategoryListVO.setLevel(goodsCategory.getLevel());
        return goodsCategoryListVO;
    }

    public static List<GoodsCategoryListVO> toVOS(List<GoodsCategory> goodsCategories) {
        if (CollectionUtils.isEmpty(goodsCategories)) {
            return CollectionUtils.emptyList();
        }
        return goodsCategories.stream().map(GoodsCategoryConverterFoVO::toVO).toList();
    }
}
