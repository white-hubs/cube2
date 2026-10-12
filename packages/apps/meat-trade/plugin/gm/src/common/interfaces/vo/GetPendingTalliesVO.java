package com.guhecloud.cube.gm.meattrade.plugin.common.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理货批次与数据
 *
 * @author lanchun
 * @date 2024/10/17 14:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetPendingTalliesVO {

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

    @Data
    @Builder
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
         * 冷热鲜标识
         */
        private String freshLabel;
    }
}
