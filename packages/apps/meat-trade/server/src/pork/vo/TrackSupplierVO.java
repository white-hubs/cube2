package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

/**
 * 商户信息
 */
@Data
public class TrackSupplierVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户编码
     */
    private String code;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 商品名称
     */
    private String productName;
}