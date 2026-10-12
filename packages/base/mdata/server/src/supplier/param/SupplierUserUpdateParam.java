package com.guhecloud.cube.biz.mdata.server.supplier.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2021/10/23
 */
@Data
public class SupplierUserUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String supplierCategoryName;

    /**
     * 手机号
     */
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
