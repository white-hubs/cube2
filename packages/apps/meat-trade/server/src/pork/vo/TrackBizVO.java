package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 供货商品跟进业务流程信息
 */
@Data
public class TrackBizVO {

    /**
     * 来货报备主键
     */
    private Long porkReportId;

    /**
     * 报备时间
     */
    private LocalDateTime porkReportTime;

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
     * 来货报备审核主键
     */
    private Long porkReportAuditId;

    /**
     * 报备审核员
     */
    private String porkReportAuditorName;

    /**
     * 报备审核结果
     */
    private PorkReportEnum porkReportAuditStatus;

    /**
     * 报备审核时间
     */
    private LocalDateTime porkReportAuditTime;

    /**
     * 登记员
     */
    private String registerName;

    /**
     * 登记时间
     */
    private LocalDateTime registerTime;

    /**
     * 检疫票张数
     */
    private Integer registerTickets;

    /**
     * 检疫票号段
     */
    private List<String> pratiqueCodes;

    /**
     * 抽检员
     */
    private String inspectorName;

    /**
     * 抽检时间
     */
    private LocalDateTime spotCheckTime;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 抽检结果
     */
    private SpotCheckEnum spotCheckStatus;

    /**
     * 理货审核员
     */
    private String porkArrangeAuditorName;

    /**
     * 理货审核时间
     */
    private LocalDateTime porkArrangeAuditTime;

    /**
     * 理货审核结果
     */
    private PorkArrangeAuditEnum porkArrangeAuditStatus;

}