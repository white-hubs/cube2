package com.guhecloud.cube.biz.adapter.park.server.application.jielink.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JieLinkParkingOutRecordReportParam {
    @JsonProperty("inRecordId")//入场记录唯一标识
    private String id;

    /**
     * 卡号
     */
    @JsonProperty("setmealNo")
    private String cardNo;

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
    @JsonProperty("outDeviceId")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("outDeviceName")
    private String passName;

    /**
     * 出场日期
     */
    @JsonProperty("outTime")
    private String date;

    @JsonProperty("outImage")
    private String outUrl;

    /**
     * 实收金额 单位分
     */
    @JsonProperty("charge")
    private String parkingFee;

    /**
     * 优惠金额 单位分
     */
    @JsonProperty("discountAmount")
    private String parkingDiscFee;

    /**
     * 操作员
     */
    @JsonProperty("stationOperator")
    private String operator;

    private Integer tenant;
}
