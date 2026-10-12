package com.guhecloud.cube.biz.adapter.park.server.application.reformer.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReformerParkingCarChargeParam {
    /**
     * 记录 ID
     */
    @JsonProperty("recordID")
    private String id;
    /**
     * 车牌号码
     */
    @JsonProperty("carCode")
    private String cardNo;
    /**
     * 进场时间（格式 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("inTime")
    private String inTime;
    /**
     * 缴费时间（格式 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("payTime")
    private String payTime;
    /**
     * 车场 ID
     */
    @JsonProperty("parkID")
    private String parkingNo;

    /**
     * 实付金额（单位：分）
     */
    @JsonProperty("paidMoney")
    private Integer paidMoney;
    /**
     * 减免金额（单位：分）
     */
    @JsonProperty("JMMoney")
    private Integer JMMoney;
    /**
     * 详见”附录 2 收费方式（chargeType）字典”
     * 0 岗亭收费
     * 1 票箱收费
     * 2 自助缴费机
     * 3 手持机
     * 4 手机 App
     * 11 接口收费
     * 1000 中央缴费
     * 1007 机器人
     * 1008 查询机
     */
    @JsonProperty("chargeType")
    private Integer chargeType;
    /**
     * 详见”附录 2 收费来源（chargeSource）字典”
     * 0 现金
     * 1 电子账户
     * 2 支付宝支付
     * 3 微信支付
     * 4 平台代扣
     * 5 喵街
     * 6 闪付
     * 7 支付宝刷卡（用户二维码被扫）
     * 8 微信刷卡（用户二维码被扫）
     * 9 银联
     * 10 ApplePay
     * 11 协商收费
     * 12 禁用
     * 13 微信公众号
     * 14 立方钱包
     * 15 积分
     * 100 交通卡
     * 101 优财收费
     * 200 未结算
     * 2000 服务窗支付宝
     * 5003 银联权益平台
     * 8000 充值卡
     * 9888 路侧
     */
    @JsonProperty("chargeSource")
    private Integer chargeSource;
    /**
     * 费用类型（停车费：0，续费：1，消费：3，充值：4）
     */
    @JsonProperty("amountType")
    private Integer amountType;

    /**
     * 总金额（单位：分）=（实付金额+减免金额）
     */
    @JsonProperty("chargeMoney")
    private Integer chargeMoney;

    /**
     * 车辆本次进场出场标识(amountType=0 或 3 不为空)
     */
    @JsonProperty("GUID")
    private String GUID;

    private Integer tenant;
}
