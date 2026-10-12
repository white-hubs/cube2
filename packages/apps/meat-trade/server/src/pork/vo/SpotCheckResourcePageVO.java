package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

/**
 * 食安溯源信息
 */
@Data
public class SpotCheckResourcePageVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 商户名称
     */
    private String supplierName;
    /**
     * 商户编码
     */
    private String supplierCode;
    /**
     * 来货报备号
     */
    private String porkReportCode;

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 报备商品主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标编码
     */
    private String footMarkCode;

    /**
     * 订单主键
     */
    private Long orderId;
}
