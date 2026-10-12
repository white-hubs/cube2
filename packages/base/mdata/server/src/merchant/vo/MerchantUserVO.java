package com.guhecloud.cube.biz.mdata.server.merchant.vo;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantUserEnum;
import lombok.Data;

import java.util.List;

/**
 * 商户用户信息
 */
@Data
public class MerchantUserVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long merchantId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 商户类别（角色）
     */
    private MerchantUserEnum merchantCategory;

    /**
     * 商家分类联系人姓名
     */
    private String merchantCategoryName;

    /**
     * 商家分类联系人电话
     */
    private String merchantCategoryPhone;

    /**
     * 商家档位
     */
    private List<String> stallNames;

    /**
     * 授权报备
     */
    private Boolean authPurchaseReport = false;

    /**
     * 授权余额支付
     */
    private Boolean authWalletPay = false;
}
