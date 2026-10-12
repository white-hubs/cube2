package com.guhecloud.cube.biz.mdata.server.merchant.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author lzy
 * @date 2021/10/23
 */
@Data
public class MerchantUserUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

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
