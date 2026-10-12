package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 大白菜理货批次与数据
 *
 * @author lanchun
 * @date 2024/9/29 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetPendingTalliesResponse {

    /**
     * 客户编号
     */
    private String merchantId;

    /**
     * 客户名称
     */
    private String merchantName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 商品编号
     */
    private String productId;

    /**
     * 报备批次信息
     */
    private List<Tally> batches;

    /**
     * 区域名称
     */
    private String areaName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tally {

        /**
         * 批次号
         */
        private String tallyId;

        /**
         * 报备数量
         */
        private Integer reportedCount;

        /**
         * 已理货数量
         */
        private Integer processedCount;

        /**
         * 报备重量
         */
        private BigDecimal reportedWeight;

        /**
         * 已理货重量
         */
        private BigDecimal processedWeight;

        /**
         * 品类id
         */
        private String goodsCategoryId;

        /**
         * 品类名称
         */
        private String goodsCategoryName;

        /**
         * 车牌号码
         */
        private String vehicleNumber;

        /**
         * 报备单号
         */
        private String billSerialNumber;

        /**
         * 养殖场
         */
        private String meatFarmName;

        /**
         * 店铺名称
         */
        private String storeName;
    }
}
