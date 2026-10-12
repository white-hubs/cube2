package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.cube.biz.mdata.client.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 批发商主键集合
     */
    private Collection<? extends Serializable> ids;

    /**
     * 用户主键集合
     */
    private Collection<? extends Serializable> userIds;

    /**
     * 批发商认证状态
     */
    private SupplierAuthenticatedEnum status;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}