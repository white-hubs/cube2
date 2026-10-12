package com.guhecloud.cube.onecard.server.consumption.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CopiesRecordExcelVo implements Serializable {
    @Excel(name = "卡号")
    private String userCard;
    @Excel(name = "交易时间", enumImportMethod = "yyyy-MM-dd HH:mm:ss")
    private String transactionTime;//交易时间
    @Excel(name = "交易份数")
    private BigDecimal transactionCount;
    @Excel(name = "交易类型")
    private String transactionType; //交易类型
    @Excel(name = "补贴开始时间", enumImportMethod = "yyyy-MM-dd HH:mm:ss")
    private String subsidyBeginDate; //补贴开始时间
    @Excel(name = "补贴结束时间", enumImportMethod = "yyyy-MM-dd HH:mm:ss")
    private String subsidyEndDate;     //补贴结束时间
    @Excel(name = "交易状态")
    private String transactionStatus; //交易状态
    @Excel(name = "交易场所")
    private String depName; //交易场所
    @Excel(name = "订单号")
    private String orderId; //订单号
    @Excel(name = "人工序号")
    private Long userSerial; //人员序号
    @Excel(name = "工号")
    private String userNo; //工号
    @Excel(name = "姓名")
    private String userName; //姓名
}
