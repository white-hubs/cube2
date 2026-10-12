package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

@Data
public class AiKeParkingV6FeeRecordReportParam {

    /**
     * 缴费单号
     */
    @JsonProperty("bill_number")
    private String id;

    /**
     * 停车场编号
     */
    @JsonProperty("parking_lot_seq")
    private String parkingNo;

    /**
     * 是否欠费补缴记录：0否，1是
     */
    @JsonProperty("arrear_back_pay")
    private String arrearBackPay;

    /**
     * 车牌号码
     */
    @JsonProperty("car_license_number")
    private String vehicleNo;

    /**
     * 入场日期
     */
    @JsonProperty("enter_time")
    private String enterTime;

    /**
     * 离场日期
     */
    @JsonProperty("leave_time")
    private String leaveTime;

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
    @JsonProperty("payment_mode")
    private Integer payMode;

    /**
     * 支付来源
     */
    @JsonProperty("pay_origin")
    private Integer payOrigin;

    /**
     * 停车场简称
     */
    @JsonProperty("parking_lot_name")
    private String parkingName;

    /**
     * 支付日期
     */
    @JsonProperty("last_pay_time")
    private String date;

    /**
     * 实收金额
     */
    @JsonProperty("actual_receivable")
    private Decimal actPayCharge;

    /**
     * 折扣金额
     */
    @JsonProperty("discount_amount")
    private Decimal discountAmount;

    /**
     * 收费员
     */
    @JsonProperty("operator")
    private String operator;

    private Integer tenant;
}
