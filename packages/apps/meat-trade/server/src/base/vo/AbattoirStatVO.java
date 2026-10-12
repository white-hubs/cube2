package com.guhecloud.cube.biz.meattrade.server.base.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 屠宰场报表统计
 *
 * @author lzy
 * @date 2021/11/22
 */
@Data
public class AbattoirStatVO {

    /**
     * 交易总片数
     */
    private Integer tradeTotalSlices;

    /**
     * 成交重量
     */
    @Decimal
    private BigDecimal dealWeight;

    /**
     * 成交金额
     */
    @Decimal
    private BigDecimal dealAmount;

    /**
     * 佣金金额
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 统计信息
     */
    private List<AbattoirStatVO.AbattoirStat> records;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 屠宰场统计信息
     */
    @Data
    public static class AbattoirStat {

        /**
         * 商品品类
         */
        private String goodsCategoryId;

        /**
         * 商品品类名称
         */
        private String goodsCategoryName;

        /**
         * 商品名称
         */
        private String productName;

        /**
         * 屠宰场名称
         */
        private String abattoirName;

        /**
         * 交易片数
         */
        private Integer tradeSlices;

        /**
         * 高于某kg数量
         */
        private Integer higherStandardWeight;

        /**
         * 低于某kg数量
         */
        private Integer lowerStandardWeight;

        /**
         * 最高价
         */
        @Decimal
        private BigDecimal higherAmount;

        /**
         * 最低价
         */
        @Decimal
        private BigDecimal lowerAmount;

        /**
         * 交易均价
         */
        @Decimal
        private BigDecimal averageAmount;

        /**
         * 实际交易均价
         */
        @Decimal
        private BigDecimal actualAmount;

        /**
         * 成交重量
         */
        @Decimal
        private BigDecimal dealWeight;

        /**
         * 成交金额
         */
        @Decimal
        private BigDecimal dealAmount;

        /**
         * 佣金金额
         */
        @Decimal
        private BigDecimal commission;
    }

}
