package com.guhecloud.cube.onecard.server.consumption.param;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AmountRecord {

    private LocalDateTime transactionTime;
    private BigDecimal transactionMoney; //交易金额
    private BigDecimal balance; //交易后 账户余额 （单位/元）
    private Integer transactionType; //交易类型 1,51 消费 2 充值 4 补贴
    private LocalDateTime subsidyBeginDate; //补贴开始时间
    private LocalDateTime subsidyEndDate;     //补贴结束时间
    private String depName;
    private Integer transactionChannel;//交易渠道 eg:电脑端
    private Integer transactionStatus; //交易状态
    private String orderId; //订单id

}
