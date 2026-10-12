package com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CbdCanteenOrderListResponse {
    private String orderId;    // 订单 id
    private String cardId;     // 餐卡类型 id
    private String cardNo;     // 卡号
    private String depId;      // 部门 id
    private String userId;     // 用户 id
    private String name;       // 姓名
    private String depName;    // 部门名称
    private String canteenName; // 食堂名称
    private String windowName; // 窗口名称
    private String goodsName;  // 商品名称
    private String getTypeStr; // 取餐类型，中文
    private String orderStatusStr; // 订单状态，中文
    private String payTypeStr; // 支付类型
    private String mealTypeStr; // 用餐类型
    private String timeTypeStr; // 餐别
    private String payType;    // 支付类型 0 微信 1 支付宝 2 电子卡 3 实体卡 4 指纹5 人脸
    private String price;      // 实付金额
    private String personalPrice; // 个人支付金额
    private String cardPrice;  // 卡支付金额
    private String cashBalance; // 储蓄卡交易后余额
    private String paymentType; // 支付类型
    private String canteenId;  // 食堂 id
    private String windowId;   // 窗口 id
    private String getType;    // 取餐类型 0：食堂取餐 1：特殊留餐 2：保温柜取餐3：送餐4 打包自提
    private String type;       // 餐卡消费类型 1:就餐消费 2:商品消费 3.买单 4.申请消费
    private String orderStatus; // 订单状态 0 待支付 1 支付失败 2.审核中 3.审核拒绝4 待用餐5已用餐 6 已过期 7 待退餐 8 已退餐 9 已取消
    private String createTime; // 创建时间
    private String planTime;   // 就餐计划时间(取餐时间)
    private String completeTime; // 就餐完成时间
    private String mealType;   // 就餐方式 1,直接就餐 2,预定就餐 3 报餐就餐
    private String startTime1; // 备用时间
    private String address;    // 地址
    private String mobile;     // 手机号码
    private String companyPrice; // 公司支付金额
    private String mealsFee;   // 餐盒费
    private String deliveryCost; // 配送费
    private String timeType;   // 餐别 1 早餐 2 午餐 3 晚餐 4 夜宵
    private String planEndTime; // 打包自提取餐截至时间
    private String payTime;    // 支付时间
    private String payMedium;  // 支付媒介：1：人脸 2：二维码 3：实体卡 4.公众号
    private String payMediumStr; // 支付媒介
    private String createUser; // 创建人
    private String dishType;   // 订餐类型 0 本人 1 代客人 2 代员工 3 代外部人员
    private String dishTypeStr; // 订餐类型中文
    private String costPrice;  // 原价
    private String discounts;  // 优惠金额
    private String time;       // 时间
    private String networkStatus; // 网络状态
    private String networkStatusStr; // 网络状态中文
    private String completeDiningVisible; // 是否可见
    private String refundAmount; // 退款金额
    private String abnormal;   // 是否异常 0正常 非0认为异常
    private String abnormalDesc; // 异常描述
    private String receivedPrice; // 实收金额
}