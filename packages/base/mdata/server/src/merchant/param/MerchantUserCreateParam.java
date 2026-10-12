package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantUserEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 商家用户修改信息
 *
 * @author lzy
 * @date 2021/10/20
 */
@Data
public class MerchantUserCreateParam {

    /**
     * 商家主键
     */
    @NotNull(message = "商家主键不能为空")
    @Min(value = 1, message = "商家主键不能小于1")
    private Long merchantId;

    /**
     * 商户类别（角色）
     */
    @NotNull(message = "商家类别不能为空")
    private MerchantUserEnum merchantCategory;

    /**
     * 商家分类联系人姓名
     */
    @NotEmpty(message = "商家分类联系人姓名不能为空")
    @Size(max = 100, message = "商家分类联系人姓名不能大于100")
    private String merchantCategoryName;

    /**
     * 商家分类联系人电话
     */
    @NotEmpty(message = "商家分类联系人电话不能为空")
    @Size(max = 100, message = "商家分类联系人电话不能大于100")
    private String merchantCategoryPhone;

    /**
     * 商家档位
     */
    private List<String> merchantStalls;

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
