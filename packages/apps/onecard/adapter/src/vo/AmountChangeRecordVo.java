package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountChangeRecordVo {

    private String userCard;
    //交易时间
    @JsonProperty(value = "jlSj")
    private String transactionTime;
    //交易金额
    @JsonProperty("newMoney")
    private String transactionMoney;
    //交易后 账户余额 （单位/元）
    @JsonProperty("saveMoney")
    private String balance;
    //交易前余额
    @JsonProperty("oldMoney")
    private String oldMoney;
    //交易类型 1, 消费 2 充值 3 补贴 4 补贴清零 5金额消费纠错 6 退款
    @JsonProperty("lx")
    private Integer transactionType;
    //交易场所
    @JsonProperty("depName")
    private String depName;
    //交易渠道 1支付宝  2 微信 3 银行卡 4 其他
    @JsonProperty("addType")
    private Integer transactionChannel;
    //交易状态
    private Integer transactionStatus;
    //订单号
    @JsonProperty("orderId")
    private String orderId;
    //人员序号
    @JsonProperty("userSerial")
    private String userSerial;
    //姓名
    @JsonProperty("userLname")
    private String userName;
    //工号
    @JsonProperty("userNo")
    private String userNo;
    //设备编号
    @JsonProperty("devSerial")
    private String devSerial;

}
