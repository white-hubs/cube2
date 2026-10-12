package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PorkArrangeQueryParam extends Paging {

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 是否完成标识
     */
    private Boolean isCompleted;

    /**
     * 理货主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 来货报备主键集合
     */
    private Collection<? extends Serializable> porkReportIds;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 时间
     */
    private Period period;

    /**
     * 市场租户编号
     */
    private Integer tenant;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 理货审核状态
     */
    private PorkArrangeAuditEnum auditEnum;
    /**
     * 供应商ID
     */
    private Long supplierId;

}
