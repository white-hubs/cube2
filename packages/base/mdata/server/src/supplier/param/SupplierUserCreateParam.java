package com.guhecloud.cube.biz.mdata.server.supplier.param;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 商家用户修改信息
 *
 * @author lzy
 * @date 2021/10/20
 */
@Data
public class SupplierUserCreateParam {

    /**
     * 商家主键
     */
    private Long supplierId;

    /**
     * 商户类别（角色）
     */
    @NotNull(message = "商家类别不能为空")
    private SupplierUserEnum supplierCategory;

    /**
     * 商家分类联系人姓名
     */
    @NotEmpty(message = "商家分类联系人姓名不能为空")
    @Size(max = 100, message = "商家分类联系人姓名不能大于100")
    private String supplierCategoryName;

    /**
     * 商家分类联系人电话
     */
    @NotEmpty(message = "商家分类联系人电话不能为空")
    @Size(max = 100, message = "商家分类联系人电话不能大于100")
    private String supplierCategoryPhone;

    /**
     * 商家档位
     */
    private List<String> supplierStalls;

    /**
     * 授权报备
     */
    @NotNull(message = "授权报备不能为空")
    private Boolean authPurchaseReport;

    /**
     * 授权余额支付
     */
    @NotNull(message = "授权余额支付不能为空")
    private Boolean authWalletPay;
}
