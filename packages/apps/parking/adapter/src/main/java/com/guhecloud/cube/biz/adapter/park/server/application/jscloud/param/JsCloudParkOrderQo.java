package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 订单信息
 * 对齐文档中的订单字段
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkOrderQo {
    @NotBlank(message = "记录id不能为空")
    private String itemId;
    @NotBlank(message = "停车场名称不能为空")
    private String parkName;
    @NotBlank(message = "停车场编号不能为空")
    private String parkCode;
    /** 车牌 */
    private String carNumber;
    /** 商户编号 */
    private String businesserCode;
    /** 商户名称 */
    private String businesserName;
    /** 订单编号 */
    private String orderNo;
    /** 订单类型 */
    private String orderType;
    /** 订单创建时间 yyyy-MM-dd HH:mm:ss */
    private String orderCreateTime;
    /** 交易流水号 */
    private String transactionId;
    /** 订单状态 */
    private Integer tradeStatus;
    /** 订单支付完成时间 yyyy-MM-dd HH:mm:ss */
    private String tradeTime;
    /** 停车时长，单位：秒 */
    @NotNull(message = "停车时长不能为空")
    private Integer serviceFeeTime;
    /** 支付方式 WX/ZFB/YL/JYF/YFB/YZF/CASH */
    private String payType;
    /** 实缴金额，单位：分 */
    private Integer totalFee;
    /** 计费开始时间 yyyy-MM-dd HH:mm:ss */
    private String inTime;
    /** 计费结束时间 yyyy-MM-dd HH:mm:ss */
    private String endTime;
    /** 停车卡号 */
    private String cardNumber;
    /** 渠道商编号 */
    private String channelCode;
    /** 应收金额，单位：分 */
    private Integer serviceFee;
    /** 商户优惠金额，单位：分 */
    private Integer discountFee;
    /** 全网优惠金额，单位：分 */
    private Integer qwDiscountFee;
    /** 支付商户号 */
    private String partnerAccount;
    /** 平台退款单号 */
    private String refundNo;
    /** 备用字段 JSON 字符串 */
    private String attach;
}

