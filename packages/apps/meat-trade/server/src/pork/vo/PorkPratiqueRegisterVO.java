package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

/**
 * @author lzy
 * @date 2022/4/12
 */
@Data
public class PorkPratiqueRegisterVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备批次号
     */
    private String porkReportCode;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 检疫票张数
     */
    private Integer pratiqueTickets;

    /**
     * 登记员主键
     */
    private Long registrantId;

    /**
     * 登记员
     */
    private String registerName;

}
