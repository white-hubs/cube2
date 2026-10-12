package com.guhecloud.cube.biz.mdata.server.supplier.vo;

import com.guhecloud.cube.biz.wallet.client.enumeration.WalletStatus;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商户账户信息
 *
 * @author appet
 * @date 2021/9/14
 */
@Data
public class SupplierAccountVO {
    /**
     * 用户ID
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
    /**
     * 商家名称
     */
    private String name;

    /**
     * 管理员用户主键
     */
    private Long adminUserId;

    /**
     * 管理员姓名
     */
    private String adminUserName;

    /**
     * 管理员
     */
    private String adminPhone;

    /**
     * 联系人
     */
    private String liaison;

    /**
     * 联系人手机号
     */
    private String phone;
}
