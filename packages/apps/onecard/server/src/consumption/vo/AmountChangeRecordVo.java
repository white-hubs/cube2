package com.guhecloud.cube.onecard.server.consumption.vo;

import lombok.Data;

@Data
public class AmountChangeRecordVo {
    private String userCard;
    private String transactionTime;//交易时间
    private String transactionMoney; //交易金额
    private String balance; //交易后 账户余额 （单位/元）
    private String transactionType; // 交易类型
    private String subsidyBeginDate; //补贴开始时间
    private String subsidyEndDate;     //补贴结束时间
    private String depName; //交易场所
    private String transactionChannel;//交易渠道 eg:电脑端
    private String transactionStatus; //交易状态
    private String orderId; //订单号
    private Long userSerial; //人员序号
    private String userNo; //工号
    private String userName; //姓名
}
