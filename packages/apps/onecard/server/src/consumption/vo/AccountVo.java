package com.guhecloud.cube.onecard.server.consumption.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountVo {
    private Long id;
    //卡号
    private String userCard;
    //人员序号
    private Long userSerial;
    //工号
    private String userNo;
    //姓名
    private String userName;
    //账户类型
    private String accountName;
    //账户余额
    private BigDecimal accountBalance;
    //账户状态
    private String accountStatus;
}
