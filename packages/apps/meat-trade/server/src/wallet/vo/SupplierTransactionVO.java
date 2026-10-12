package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.wallet.client.enumeration.MoneyFlow;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户交易记录信息
 *
 * @author Woody
 * @date 2021/9/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierTransactionVO {
    /**
     * 资金流向
     */
    private MoneyFlow flow;

    /**
     * 业务类型
     */
    private Business business;

    /**
     * 转出商户名称
     */
    private SupplierVO sourceMerchant;

    /**
     * 转入商户名称
     */
    private SupplierVO targetMerchant;

    /**
     * 交易金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 交易备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 业务信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Business {
        /**
         * 业务主键
         */
        private Long id;

        /**
         * 业务类型
         */
        private String type;

        /**
         * 业务名称
         */
        private String name;
    }
}
