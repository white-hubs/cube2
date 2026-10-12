package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品采价记录三方返回对象
 */
@Data
public class GoodsPricingRecordThirdVO {
    Long id;
    /**
     * 采价任务名称
     */
    private String taskName;
    /**
     * 采价员ID
     */
    private Long userId;
    /**
     * 采价员姓名
     */
    private String userName;
    /**
     * 商品品类ID
     */
    private Long goodsCategoryId;
    /**
     * 商品品类名称
     */
    private String goodsCategoryName;
    /**
     * 商品品类序列号
     */
    private String goodsCategorySequence;
    /**
     * 品类父级id
     */
    private Long goodsCategoryParentId;
    /**
     * 商品来源（产地）
     */
    private String goodsFrom;
    /**
     * 最高价
     */
    private BigDecimal maxPrice;
    /**
     * 中间价
     */
    private BigDecimal midPrice;
    /**
     * 最低价
     */
    private BigDecimal minPrice;
    /**
     * 采价时间
     */
    private LocalDateTime createTime;
}
