package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.wallet.client.enumeration.WalletStatus;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户账户信息
 *
 * @author Woody
 * @date 2021/9/14
 */
@Data
public class SupplierAccountVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 钱包状态
     */
    private WalletStatus status;

    /**
     * 激活时间
     */
    private LocalDateTime activateTime;

    /**
     * 是否开启免密支付
     */
    private Boolean paymentQuickly;

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

    /**
     * 商户信息
     */
    private SupplierDTO supplier;
}
