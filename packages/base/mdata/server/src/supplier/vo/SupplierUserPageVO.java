package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import lombok.Data;

import java.util.List;

/**
 * 商户用户列表信息
 *
 * @author lzy
 * @date 2021/10/20
 */
@Data
public class SupplierUserPageVO {

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
     * 商家档位集合
     */
    private List<Stall> stalls;

    /**
     * 授权报备
     */
    private Boolean authPurchaseReport = false;

    /**
     * 授权余额支付
     */
    private Boolean authWalletPay = false;

    /**
     * 档位信息
     */
    @Data
    public static class Stall {

        /**
         * 主键
         */
        private Long id;

        /**
         * 名称
         */
        private String name;

    }

}
