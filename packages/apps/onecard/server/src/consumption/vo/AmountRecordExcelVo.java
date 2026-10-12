package com.guhecloud.cube.onecard.server.consumption.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class AmountRecordExcelVo implements Serializable {
    @Excel(name = "卡号")
    private String userCard;
    @Excel(name = "交易时间", enumImportMethod = "yyyy-MM-dd HH:mm:ss")
    private String transactionTime;//交易时间
    @Excel(name = "交易金额（元）")
    private String transactionMoney; //交易金额
    @Excel(name = "交易后金额（元）")
    private String balance; //交易后 账户余额 （单位/元）
    @Excel(name = "交易类型")
    private String transactionType; //交易类型
    @Excel(name = "交易场所")
    private String depName; //交易场所
    @Excel(name = "交易渠道")
    private String transactionChannel;//交易渠道 eg:电脑端
    @Excel(name = "交易状态")
    private String transactionStatus; //交易状态
    @Excel(name = "订单号")
    private String orderId; //订单号
    @Excel(name = "人工序号")
    private String userSerial; //人员序号
    @Excel(name = "工号")
    private String userNo; //工号
    @Excel(name = "姓名")
    private String userName; //姓名
}
