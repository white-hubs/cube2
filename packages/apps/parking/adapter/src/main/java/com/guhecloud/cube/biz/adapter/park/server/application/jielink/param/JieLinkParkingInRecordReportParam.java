package com.guhecloud.cube.biz.adapter.park.server.application.jielink.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JieLinkParkingInRecordReportParam {
    @JsonProperty("inRecordId")//入场记录唯一标识
    private String id;

    /**
     * 套餐编号
     */
    @JsonProperty("setmealNo")
    private String cardNo;

    /**
     * 套餐名称
     */
    @JsonProperty("sealName")
    private String cardName;
    /**
     * 车牌号码
     */
    @JsonProperty("plateNumber")
    private String vehicleNo;

    /**
     * 车牌颜色
     */
    @JsonProperty("plateColor")
    private String vehicleColor;

    /**
     * 停车场编号
     */
    @JsonProperty("parkId")
    private String parkingNo;

    /**
     * 通道编号
     */
    @JsonProperty("inDeviceId")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("inDeviceName")
    private String passName;

    /**
     * 入场日期
     */
    @JsonProperty("inTime")
    private String date;

    @JsonProperty("inImage")
    private String inUrl;

    /**
     * 操作员
     */
    @JsonProperty("stationOperator")
    private String operator;

    private Integer tenant;
}
