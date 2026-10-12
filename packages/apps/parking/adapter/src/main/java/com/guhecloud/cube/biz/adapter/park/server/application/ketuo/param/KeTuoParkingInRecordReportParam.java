package com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeTuoParkingInRecordReportParam {
    @JsonProperty("trafficId")
    private String id;

    /**
     * 人员编号
     */
    @JsonProperty("perno")
    private String perNo;

    /**
     * 人员姓名
     */
    @JsonProperty("pername")
    private String perName;

    /**
     * 卡号
     */
    @JsonProperty("cardno")
    private String cardNo;

    /**
     * 车牌号码
     */
    @JsonProperty("plateNo")
    private String vehicleNo;

    /**
     * 车辆模式
     */
    @JsonProperty("vehiclemode")
    private Integer vehicleMode;

    /**
     * 停车场编号
     */
    @JsonProperty("parkId")
    private String parkingNo;

    /**
     * 停车场简称
     */
    @JsonProperty("parkName")
    private String parkingName;

    /**
     * 通道编号
     */
    @JsonProperty("deviceID")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("entryPlace")
    private String passName;

    /**
     * 入场日期
     */
    @JsonProperty("entryTime")
    private String date;

    /**
     * 入场时间
     */
    @JsonProperty("time")
    private String time;

    /**
     * 入场图片URL地址
     */
    @JsonProperty("imgName")
    private String inUrl;

    /**
     * 入场图片(base64)
     */
    @JsonProperty("inimg")
    private String inImg;

    /**
     * 操作员
     */
    @JsonProperty("operator")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("carColor")
    private String color;

    /**
     * 车辆类型
     */
    @JsonProperty("vehicleBrand")
    private String vehicleType;

    private Integer tenant;

    private CarInExt carInExt;
}
