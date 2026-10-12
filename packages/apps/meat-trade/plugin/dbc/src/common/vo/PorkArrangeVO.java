package com.guhecloud.cube.sn.meattrade.plugin.common.vo;

import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/7
 */
@Data
public class PorkArrangeVO {

    /**
     * 主键
     */
    private Long id;

    private Long supplierId;

    /**
     * 批发商名称
     */
    private String supplierName;

    /**
     * 批发商编码
     */
    private String supplierCode;

    /**
     * 风险级别
     */
    private String riskLevel;

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 来货报备批次号
     */
    private String porkReportCode;

    /**
     * 来货报备片数
     */
    private Integer porkReportSlices;

    /**
     * 来货报备重量
     */
    @Decimal
    private BigDecimal porkReportWeight;

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
    private String freshLabel;

    /**
     * 档位名称
     */
    private String stallName;

    /**
     * 屠宰场主键
     */
    private Long abattoirId;

    /**
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 养殖场
     */
    private String originPlace;

    /**
     * 检疫票登记主键
     */
    private Long pratiqueRegisterId;

    /**
     * 检疫票数
     */
    private Integer pratiqueTickets;

    /**
     * 检疫票号集合
     */
    private List<String> pratiqueCodes;

    /**
     * 登记员
     */
    private String registrantName;

    /**
     * 食安检验主键
     */
    private Long spotCheckId;

    /**
     * 抽检结果
     */
    private String spotCheckStatus;

    /**
     * 瘦肉精
     */
    private String clenbuterol;

    /**
     * 含水量
     */
    private String watered;

    /**
     * 抽检员
     */
    private String inspectorName;

    /**
     * 理货数据主键
     */
    private Long porkArrangeId;

    /**
     * 理货片数
     */
    private Integer porkArrangeSlices;

    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal porkArrangeWeight;

    /**
     * 理货状态
     */
    private String porkArrangeStatus;

    /**
     * 理货点
     */
    private String porkArrangePlace;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否可以理货审核
     */
    private Boolean mark;

    /**
     * 市场方租户编号
     */
    private Integer tenant;

    /**
     * 推送状态
     */
    private PorkArrangePushDataStatusEnum pushStatus;
}
