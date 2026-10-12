package com.guhecloud.cube.onecard.server.consumption.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("account_details")
/**
 * @Date 2023/2/24 14:17
 * @Author tangzhiqiang
 */
public class AccountDetails extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 卡号
     */
    private String userCard;

    /**
     * 补贴余额
     */
    private BigDecimal subsidyBalance;

    /**
     * 现金余额
     */
    private BigDecimal cashBalance;

    /**
     * 理发卷金额
     */
    private BigDecimal haircutCount;

    /**
     * 洗衣次数
     */
    private Integer laundryCount;
}
