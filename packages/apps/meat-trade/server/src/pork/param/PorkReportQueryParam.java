package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PorkReportQueryParam extends Paging {

    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 报备主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 报备状态
     */
    private PorkReportEnum status;

    /**
     * 报备时间
     */
    private Period period;

    /**
     * 市场方租户
     */
    private Integer tenant;

    /**
     * 是否审核
     */
    private Boolean isAudited;

}
