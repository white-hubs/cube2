package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 资产汇总信息
 *
 * @author Woody
 * @date 2021/9/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsSummaryVO {
    /**
     * 总账金额
     */
    @Decimal
    private BigDecimal total;

    /**
     * 市场总额
     */
    @Decimal
    private BigDecimal platform;

    /**
     * 商家总额
     */
    @Decimal
    private BigDecimal merchant;
}
