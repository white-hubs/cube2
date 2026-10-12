package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AiKeParkingOutRecordReportParam {
    @JsonProperty("parkingCode")
    private String id;

    /**
     * 卡号
     */
    @JsonProperty("leaveCarCardNumber")
    private String cardNo;

    /**
     * 车牌号码
     */
    @JsonProperty("carLicenseNumber")
    private String vehicleNo;

    /**
     * 停车场编号
     */
    @JsonProperty("parkCode")
    private String parkingNo;

    /**
     * 停车场简称
     */
    @JsonProperty("parkName")
    private String parkingName;

    /**
     * 通道编号
     */
    @JsonProperty("leaveChannelCustomCode")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("leaveChannelName")
    private String passName;

    /**
     * 出场日期
     */
    @JsonProperty("leaveTime")
    private String date;

    /**
     * 出场图片URL地址
     */
    @JsonProperty("leaveImageArray")
    private JSONArray leaveImageArray;

    private String outUrl;

    /**
     * 实收金额
     */
    @JsonProperty("actualReceivable")
    private String parkingFee;

    /**
     * 优惠金额
     */
    @JsonProperty("discountAmount")
    private String parkingDiscFee;

    /**
     * 操作员
     */
    @JsonProperty("outOperatorName")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("leaveCarColor")
    private Integer color;

    /**
     * 车辆类型
     */
    @JsonProperty("leaveCarType")
    private Integer vehicleType;

    private Integer tenant;
}
