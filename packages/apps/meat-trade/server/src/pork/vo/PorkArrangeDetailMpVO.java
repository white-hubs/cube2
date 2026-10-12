package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class PorkArrangeDetailMpVO {

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 商品品类主键
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
     * 理货审核状态
     */
    private PorkArrangeAuditEnum auditStatus;

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 理货片数
     */
    private Integer porkArrangeSlices;

    /**
     * 理货重量
     */
    private BigDecimal porkArrangeWeight;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

    /**
     * 不通过原因
     */
    private String remark;

    /**
     * 理货详情数据
     */
    private List<PorkArrangeDetailVO> records;

}
