package com.guhecloud.cube.onecard.server.consumption.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CopiesRecord {

    private LocalDateTime transactionTime;
    private Integer transactionCount; //交易份数
    private Integer transactionType; //交易类型 1,51 消费 2 充值 4 补贴
    private LocalDateTime subsidyBeginDate; //补贴开始时间
    private LocalDateTime subsidyEndDate;  //补贴结束时间
    private String depName;
    private Integer transactionStatus; //交易状态
}
