package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarSafeParkingChargeRecordReportParam {

    /**
     * 场内车序号（进出记录序号）
     */
    @JsonProperty("cardInId")
    private String cardInId;

    /**
     * 出场日期
     */
    @JsonProperty("outDate")
    private String date;

    /**
     * 车牌号
     */
    @JsonProperty("plateNo")
    private String vehicleNo;

    /**
     * 停车时长（单位分钟，取整）
     */
    @JsonProperty("parktime")
    private Integer parkingTimes;

    /**
     * 实收金额（单位元，两位小数）
     */
    @JsonProperty("amount")
    private BigDecimal amount;

    /**
     * 收费序列号
     */
    @JsonProperty("chargeSeqNo")
    private String chargeSeqNo;

    /**
     * 订单编号。如果停车费用为0就不会生成订单。生成的订单如果在15分钟（可配置）之内未缴费，将会被系统自动取消。
     */
    @JsonProperty("orderNo")
    private String orderNo;

    /**
     * 扫码入场的openid
     */
    @JsonProperty("openId")
    private String openId;

    /**
     * 进场时间（格式化字符串：2015-05-13 13:00:00）
     */
    @JsonProperty("entertime")
    private String entertime;

    /**
     * 停车费用（应收金额,单位元，两位小数）
     */
    @JsonProperty("parkcost")
    private BigDecimal parkcost;

    /**
     * 优惠金额（单位元，两位小数）
     */
    @JsonProperty("discountAmount")
    private BigDecimal discountAmount;

    /**
     * 计费开始时间。如果入场后还未缴费，时间与进场时间一致；如果之前缴过费但未出场，此时间为上次缴费的时间。
     */
    @JsonProperty("calcStartTime")
    private String calcStartTime;

    /**
     * 计费结束时间
     */
    @JsonProperty("calcEndTime")
    private String calcEndTime;

    /**
     * 交费类型  (51：通联支付50:银联全民付 49:银联在线支付(网付通) 48:杉德支付 46:威富通扫码支付 45:招商银行支付 44:中国银行支付43:上海停车支付宝端42:上海停车微信端 41:上海停车APP 40:第三方无感支付 39:交通银行支付 38:杭州银行主扫支付 37:P+R支付 36:POS通主扫支付 35:微信无感支付 31:农行主扫支付 30:农行无感支付 21:万集ETC支付  22:驿亭支付 20:银联无感支付 19:建设银行银联支付 18:建设银行支付宝支付 17:建设银行微信支付 16:建设银行龙支付 15:建设银行无感支付 14:POS通支付宝支付 13:POS通微信支付 12:先行后付 11:支付宝免密支付 10:第三方支付 8:预约支付 7:微信支付 6:羊城通支付 5:支付宝支付 4：银联POS机支付 3：储值卡支付 2：现金支付)。默认为第三方支付
     */
    @JsonProperty("payType")
    private String payType;

    /**
     * 支付系统（如：支付宝、微信等）的交易订单号
     */
    @JsonProperty("tradeNo")
    private String tradeNo;

    /**
     * 支付系统（如：支付宝、微信等）的交易日期
     */
    @JsonProperty("tradeTime")
    private String tradeTime;

    /**
     * 支付状态（0: 等待付款 1: 付款成功 2: 取消付款）
     */
    @JsonProperty("status")
    private String status;

    /**
     * 租户信息
     */
    private Integer tenant;
}
