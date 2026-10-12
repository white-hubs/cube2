package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PratiqueRegisterQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 来货报备主键
     */
    private Collection<? extends Serializable> porkReportIds;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 登记时间
     */
    private Period registerTime;

    /**
     * 是否登记
     */
    private Boolean status;

    /**
     * 时间
     */
    private Period period;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
