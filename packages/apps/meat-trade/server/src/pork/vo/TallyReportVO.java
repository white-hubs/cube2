package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

/**
 * 理货机理货批次数据
 */
@Data
public class TallyReportVO {

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 商品品类
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 报备号
     */
    private String tallyId;

    /**
     * 报备数量
     */
    private String reportedCount;

    /**
     * 已理货数量
     */
    private String processedCount;

    /**
     * 报备重量
     */
    private String reportedWeight;

    /**
     * 已理货重量
     */
    private String processedWeight;

}
