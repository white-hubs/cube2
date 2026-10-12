package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

@Data
public class AiKeParking2FeeRecordReportParam {
    @JsonProperty("parkingCode")
    private String id;

    /**
     * 停车场编号
     */
    @JsonProperty("parkCode")
    private String parkingNo;

    /**
     * 如通过开放平台缴费接口缴费，则为缴费时的billCode；否则为停车场端的缴费订单号
     */
    @JsonProperty("billCode")
    private String billCode;

    /**
     *
     */
    @JsonProperty("cmd")
    private String cmd;

    /**
     * 是否欠费补缴记录：0否，1是
     */
    @JsonProperty("isArrear")
    private String isArrear;

    /**
     * 支付方式
     * 0
     * 未知
     * 1
     * 现金
     * 2
     * 自发卡
     * 3
     * 次数
     * 4
     * 微信支付
     * 5
     * 支付宝支付
     * 6
     * 银联闪付
     * 7
     * 交通卡
     * 8
     * 免费
     * 9
     * 优惠
     * 10
     * 银联接触卡
     * 11
     * 银联网络支付
     * 12
     * 第三方余额
     * 13
     * NFC
     * 14
     * 普通储蓄卡
     * 15
     * 信用卡
     * 16
     * 微信免密
     * 17
     * 支付宝免密
     * 18
     * 腾讯无感支付
     * 19
     * ETC
     * 20
     * 手动转账
     * 21
     * 微信无感支付
     * 22
     * 支付宝无感支付
     * 23
     * 银联无感支付
     * 24
     * 转账（仅适用于线上月票）
     * 25
     * 刷卡（仅适用于线上月票）
     */
    @JsonProperty("payMode")
    private Integer payMode;

    /**
     * 支付来源
     */
    @JsonProperty("payOrigin")
    private Integer payOrigin;

    /**
     * 平台应补扣金额，自动扣费时使用
     */
    @JsonProperty("platformShouldPay")
    private Decimal platformShouldPay;

    /**
     * 停车场简称
     */
    @JsonProperty("parking_lot_name")
    private String parkingName;

    /**
     * 出场日期
     */
    @JsonProperty("payDate")
    private String date;

    /**
     * 实收金额
     */
    @JsonProperty("actPayCharge")
    private Decimal actPayCharge;

    /**
     * 优惠金额
     */
    @JsonProperty("reliefCharge")
    private Decimal reliefCharge;

    /**
     * 收费员
     */
    @JsonProperty("collectorName")
    private String operator;

    private Integer tenant;
}
