package com.guhecloud.cube.onecard.server.consumption.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("amount_change_record")
public class AmountChangeRecord extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String userCard;
    private LocalDateTime transactionTime;//交易时间
    private BigDecimal transactionMoney; //交易金额
    private BigDecimal balance; //交易后 账户余额 （单位/元）
    private Integer transactionType; //交易类型 1, 消费 2 充值 3 补贴 4 补贴清零 5金额消费纠错 6 退款
    private LocalDateTime subsidyBeginDate; //补贴开始时间
    private LocalDateTime subsidyEndDate;     //补贴结束时间
    private String depName; //交易场所
    private Integer transactionChannel;//交易渠道 1支付宝  2 微信 3 银行卡 4 其他
    private Integer transactionStatus; //交易状态
    private String orderId; //订单号

}
