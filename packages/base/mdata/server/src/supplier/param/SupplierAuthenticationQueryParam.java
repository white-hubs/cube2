package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * @author lzy
 * @date 2021/12/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SupplierAuthenticationQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 认证类型
     */
    private SupplierAuthenticatedEnum status;

    /**
     * 认证状态
     */
    private PurchaserTypeEnum type;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
