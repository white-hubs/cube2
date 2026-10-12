package com.guhecloud.cube.biz.mdata.client.dto;

import com.guhecloud.cube.biz.mdata.client.enumeration.SupplierUserEnum;
import lombok.Data;

import java.util.List;

/**
 * 商户用户信息
 */
@Data
public class SupplierUserDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 商户类别（角色）
     */
    private SupplierUserEnum supplierCategory;

    /**
     * 商家分类联系人姓名
     */
    private String supplierCategoryName;

    /**
     * 商家分类联系人电话
     */
    private String supplierCategoryPhone;

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
