package com.guhecloud.cube.biz.meattrade.server.base.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

import java.util.Set;

@Data
public class GoodsWeightConfigVO {

    private Long id;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 商品主键集合
     */
    private Set<String> goodsCategoryIds;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 标准重量
     */
    private Double standardWeight;

    /**
     * 重量单位
     */
    private String weightUnit;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 备注信息
     */
    private String remark;
}
