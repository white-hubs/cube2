package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
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
public class SpotCheckQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字")
    private String keyword;

    /**
     * 食安抽检主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 来货报备主键
     */
    private Collection<? extends Serializable> porkReportIds;

    /**
     * 批发商信息主键
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 瘦肉精
     */
    private SpotCheckEnum clenbuterol;

    /**
     * 含水量
     */
    private SpotCheckEnum watered;

    /**
     * 是否抽检
     */
    private Boolean status;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 时间
     */
    private Period period;

}
