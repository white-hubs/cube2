package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户资产信息
 *
 * @author Woody
 * @date 2021/9/14
 */
@Data
public class AccountAssetsVO {
    /**
     * 账户资产
     */
    @Decimal
    private BigDecimal assets;

    /**
     * 账户余额
     */
    @Decimal
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    @Decimal
    private BigDecimal frozen;
}
