package com.guhecloud.subsystem.requester.sdk.vo.onecard;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 食堂消费数据
 */
@Data
public class CanteenOrderListVO {
    private String orderId;    // 订单 id
    private String cardId;     // 餐卡类型 id
    private String cardNo;     // 卡号
    private String depId;      // 部门 id
    private String userId;     // 用户 id
    private String name;       // 姓名
    private String depName;    // 部门名称
    private String canteenName; // 食堂名称
    private String windowName; // 窗口名称
    private String orderStatusStr; // 订单状态，中文
    private String payTypeStr; // 支付类型
    private String payType;    // 支付类型 0 微信 1 支付宝 2 电子卡 3 实体卡 4 指纹5 人脸
    private BigDecimal price;      // 实付金额
    private BigDecimal personalPrice; // 个人支付金额
    private BigDecimal cardPrice;  // 卡支付金额
    private BigDecimal cashBalance; // 储蓄卡交易后余额
    private String paymentType; // 支付类型
    private String canteenId;  // 食堂 id
    private String windowId;   // 窗口 id
    private String orderStatus; // 订单状态 0 待支付 1 支付失败 2.审核中 3.审核拒绝4 待用餐5已用餐 6 已过期 7 待退餐 8 已退餐 9 已取消
    private String createTime; // 创建时间
    private String payTime;    // 支付时间
    private BigDecimal costPrice;  // 原价
    private BigDecimal discounts;  // 优惠金额
    private String time;       // 时间
    private BigDecimal refundAmount; // 退款金额
    private String abnormal;   // 是否异常
    private String abnormalDesc; // 异常描述
    private BigDecimal receivedPrice; // 实收金额
}