package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 供货商品跟进信息
 */
@Data
public class TrackPageVO {

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
     * 商品名称
     */
    private String productName;

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 报备片数
     */
    private Integer porkReportSlices;

    /**
     * 报备重量
     */
    @Decimal
    private BigDecimal porkReportWeight;

    /**
     * 来货报备审核状态
     */
    private PorkReportEnum porkReportStatus;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 理货审核主键
     */
    private Long porkArrangeAuditId;

    /**
     * 理货审核状态
     */
    private PorkArrangeAuditEnum porkArrangeAuditStatus;

}