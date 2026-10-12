package com.guhecloud.cube.biz.meattrade.server.base.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 屠宰场报表统计
 *
 * @author lzy
 * @date 2021/11/23
 */
@Data
public class AbattoirStatDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 交易片数
     */
    private Integer tradeSlices;

    /**
     * 高于某kg数量
     */
    private Integer higherStandardWeight;

    /**
     * 低于某kg数量
     */
    private Integer lowerStandardWeight;

    /**
     * 最高价
     */
    private BigDecimal higherAmount;

    /**
     * 最低价
     */
    private BigDecimal lowerAmount;

    /**
     * 交易均价
     */
    private BigDecimal averageAmount;

    /**
     * 实际交易均价
     */
    private BigDecimal actualAmount;
    private BigDecimal actualWeight;

    /**
     * 成交重量
     */
    private BigDecimal dealWeight;

    /**
     * 成交金额
     */
    private BigDecimal dealAmount;

    /**
     * 佣金金额
     */
    private BigDecimal commission;

    /**
     * 商品品类
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;

}
