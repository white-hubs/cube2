package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.RechargeStatus;
import com.guhecloud.cube.biz.wallet.client.enumeration.RechargeMode;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户充值记录信息
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
public class SupplierDepositVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 充值方式
     */
    private RechargeMode mode;

    /**
     * 充值金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 充值状态
     */
    private RechargeStatus status;

    /**
     * 商户信息
     */
    private SupplierDTO supplier;
}
