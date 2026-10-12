package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.mdata.client.temp.RiskLevelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class PratiqueRegisterVO {

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
     * 批发商编号
     */
    private String supplierCode;

    /**
     * 风险级别
     */
    private RiskLevelEnum riskLevel;

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 报备号
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
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 报备片数
     */
    private Integer porkReportSlices;

    /**
     * 登记员主键
     */
    private Long registrantId;

    /**
     * 登记员名称
     */
    private String registrantName;

    /**
     * 检疫片数
     */
    private Integer pratiqueSlices;

    /**
     * 检疫票数
     */
    private Integer pratiqueTickets;

    /**
     * 检疫证号
     */
    private String pratiqueCode;

    /**
     * 检疫证号集合
     */
    private List<String> pratiqueCodes;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否登记
     */
    private Boolean status;

    /**
     * 登记时间
     */
    private LocalDateTime registerTime;

    /**
     * 是否可以登记
     */
    private Boolean mark;

}
