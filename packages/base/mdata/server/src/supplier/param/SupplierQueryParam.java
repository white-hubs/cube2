package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.PurchaserTypeEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
     * 批发商认证类型
     */
    private PurchaserTypeEnum type;

    /**
     * 市场租户编号
     */
    private Integer tenant;
}
