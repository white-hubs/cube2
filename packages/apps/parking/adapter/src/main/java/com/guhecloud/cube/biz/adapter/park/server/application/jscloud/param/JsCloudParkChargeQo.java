package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 收费记录
 * 接口协议: 数据中心平台主动推送收费记录数据到第三方平台
 * 接收地址: http://serverUrl/park/parkCharge
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkChargeQo {
    /** 收费记录ID，必填，长度32 */
    @NotBlank(message = "记录id不能为空")
    private String itemId;
    /** 停车场名称，必填，长度30 */
    @NotBlank(message = "停车场名称不能为空")
    private String parkName;
    /** 停车场编号，必填，长度30 */
    @NotBlank(message = "停车场编号不能为空")
    private String parkCode;
    /** 车牌，可选，长度10，异常情况时无车牌 */
    private String carNumber;
    /** 收费时间，必填，格式yyyy-MM-dd HH:mm:ss */
    @NotBlank(message = "收费时间不能为空")
    private String feesTime;
    /** 应收金额，必填，单位元 */
    @NotNull(message = "应收金额不能为空")
    private Double ysMoney;
    /** 优惠金额，可选，单位元 */
    private Double yhMoney;
    /** 实收金额，必填，单位元 */
    @NotNull(message = "实收金额不能为空")
    private Double ssMoney;
    /** 全网优惠券金额，可选，单位元 */
    private Double netDiscountMoney;
    /** 回滚减免金额，可选，单位元 */
    private Double hgMoney;
    /** 入场时间，可选，格式yyyy-MM-dd HH:mm:ss，异常时无值 */
    private String inTime;
    /** 收费操作员ID，可选，长度30 */
    private String operatorId;
    /** 收费操作员名称，可选，长度30 */
    private String operatorName;
    /** 收费设备id，可选，长度32 */
    private String equipmentId;
    /** 车辆入场记录ID，必填，长度20 */
    @NotBlank(message = "车辆入场记录ID不能为空")
    private String parkInId;
    /** 付款方式，可选，长度30，见数据字典的支付方式 */
    private String payType;
    /** 支付来源，可选，长度30，如jieshun,third */
    private String payFrom;
    /** 代扣标识，可选，0:无代扣,1:代扣 */
    private Integer replaceDeduct;
    /** APP用户ID，可选，长度30，代扣时必须提供 */
    private String appUserId;
    /** 套餐ID，可选，长度30 */
    private String userTypeId;
    /** 套餐名称，可选 */
    private Integer userTypeName;
    /** 卡ID，可选，长度100 */
    private String cardId;
    /** 备用字段，可选，长度100，json格式字符串 */
    private String attach;
}
