package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AiKeParkingInRecordReportParam {
    @JsonProperty("parkingCode")//数据唯一标识
    private String id;

    /**
     * 卡号
     */
    @JsonProperty("enterCarCardNumber")
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
    @JsonProperty("enterChannelCustomCode")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("enterChannelName")
    private String passName;

    /**
     * 入场日期
     */
    @JsonProperty("enterTime")
    private String date;

    /**
     * 入场图片URL地址
     */
    @JsonProperty("enterImageArray")
    private JSONArray enterImageArray;

    private String inUrl;

    /**
     * 操作员
     */
    @JsonProperty("inOperatorName")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("enterCarColor")
    private Integer color;

    /**
     * 车辆类型
     */
    @JsonProperty("enterCarType")
    private Integer vehicleType;

    private Integer tenant;
}
