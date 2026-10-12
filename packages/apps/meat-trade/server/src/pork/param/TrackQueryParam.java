package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 供货商品跟进查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrackQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 来货报备状态
     */
    private PorkReportEnum porkReportStatus;

    /**
     * 理货审核状态
     */
    private PorkArrangeAuditEnum porkArrangeAuditStatus;

    /**
     * 时间
     */
    private Period period;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 商户主键集合
     */
    private Collection<Long> supplierIds;

    /**
     * 来货报备信息主键集合
     */
    private Collection<Long> porkReportIds;

    /**
     * 理货审核主键信息集合
     */
    private Collection<Long> porkArrangeAuditIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}