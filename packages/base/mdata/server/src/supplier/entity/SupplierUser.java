package com.guhecloud.cube.biz.mdata.server.supplier.entity;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
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
public class SupplierUser {

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
    private String supplierStallId;

    /**
     * 商家档位名称
     */
    private String supplierStallName;

    /**
     * 授权报备
     */
    private Boolean authPurchaseReport = false;

    /**
     * 授权余额支付
     */
    private Boolean authWalletPay = false;

}
