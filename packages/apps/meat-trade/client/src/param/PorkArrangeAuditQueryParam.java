package com.guhecloud.cube.biz.meattrade.client.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author lzy
 * @date 2021/12/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PorkArrangeAuditQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 理货状态
     */
    private String arrangeStatus;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 是否审核
     */
    private Boolean status;

    /**
     * 是否合格(1:合格,0:不合格)
     */
    private Boolean spotCheckResult;

    /**
     * 来货报备主键信息
     */
    private Collection<? extends Serializable> porkReportIds;

    /**
     * 理货数据主键集合
     */
    private Collection<? extends Serializable> porkArrangeIds;

    /**
     * 来货报备号集合
     */
    private Collection<? extends Serializable> porkReportCodes;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 审核时间
     */
    private Period auditPeriod;

    /**
     * 理货设备集合
     */
    private Set<String> deviceIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
