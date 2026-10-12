package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpotCheckResourceQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> supplierIds;

    /**
     * 理货详情主键集合
     */
    private Collection<? extends Serializable> porkArrangeDetailIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
