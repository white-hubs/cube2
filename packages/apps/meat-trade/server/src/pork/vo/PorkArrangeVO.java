package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class PorkArrangeVO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 报备主键
     */
    private Long porkReportId;
    /**
     * 批次号
     */
    private String porkReportCode;
    /**
     * 理货片数
     */
    private Integer slices;
    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal weight;
    /**
     * 理货片数
     */
    private Integer porkReportSlices;
    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal porkReportWeight;
    /**
     * 理货状态
     */
    private PorkArrangeEnum status;
    /**
     * 理货审核状态
     */
    private PorkArrangeAuditEnum auditStatus;
    /**
     * 来货报备时间
     */
    private LocalDateTime porkReportTime;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

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
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 备注
     */
    private String remark;

}
