package com.guhecloud.cube.onecard.server.consumption.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/18 14:02
 * @Description: 消费数据实体
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("consume_record_out")
public class ConsumeRecordOut extends BasicEnhancedTenantEntity<Long> {

    /**
     * 系统编号
     */
    private Long id;

    /**
     * 记录类型
     */
    private String recordType;
    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 物理卡号
     */
    private String cardNumber;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 账户类型名称
     */
    private String type;

    /**
     * 消费类型
     */
    private String mode;

    /**
     * 设备名称
     */
    private String device;

    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;

    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;

    /**
     * 补贴金额
     */
    private BigDecimal subsidyAmount;

    /**
     * 交易次数
     */
    private Long times;
}
