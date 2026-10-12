package com.guhecloud.cube.biz.mdata.server.merchant.entity;

import com.guhecloud.cube.biz.mdata.server.merchant.enumeration.MerchantUserEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商户用户信息
 *
 * @author lzy
 * @date 2021/10/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantUser {

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
    private String merchantStallId;

    /**
     * 商家档位名称
     */
    private String merchantStallName;

    /**
     * 授权报备
     */
    private Boolean authPurchaseReport = false;

    /**
     * 授权余额支付
     */
    private Boolean authWalletPay = false;

}
