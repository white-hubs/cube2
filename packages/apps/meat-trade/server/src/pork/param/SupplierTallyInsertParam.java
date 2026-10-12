package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户理货数据新增
 */
@Data
public class SupplierTallyInsertParam {

    /**
     * 理货机编号
     */
    private String deviceId;

    /**
     * 商户标识类型
     */
    private String midType;

    /**
     * 商户手机号
     */
    private String midPhone;

    /**
     * 商户名称
     */
    private String midName;

    /**
     * 商户编号
     */
    private String midCode;

    /**
     * 批次号
     */
    private String tallyId;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 区域信息
     */
    private String areaName;

    /**
     * 报备数量
     */
    private Integer reportedCount;

    /**
     * 报备重量
     */
    private BigDecimal reportedWeight;

    /**
     * 屠宰场
     */
    private String meatFarmName;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;
}
