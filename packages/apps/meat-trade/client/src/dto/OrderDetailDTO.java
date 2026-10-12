package com.guhecloud.cube.biz.meattrade.client.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lzy
 * @date 2022/1/21
 */
@Data
public class OrderDetailDTO {

    /**
     * 理货详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 损耗
     */
    private BigDecimal loss;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     * 商品主键
     */
    private String goodsCategoryId;

    /**
     * 商品名称
     */
    private String goodsCategoryName;
}
