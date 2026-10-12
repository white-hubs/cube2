package com.guhecloud.cube.onecard.server.consumption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("account")
/**
 * @Date 2023/2/24 11:47
 * @Author tangzhiqiang
 */
public class Account extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 卡号
     */
    private String userCard;

    /**
     * 人员序号
     */
    private Long userSerial;

    /**
     * 工号
     */
    private String userNo;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 账户类型
     */
    private String accountName;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 账户状态
     */
    private String accountStatus;

}
