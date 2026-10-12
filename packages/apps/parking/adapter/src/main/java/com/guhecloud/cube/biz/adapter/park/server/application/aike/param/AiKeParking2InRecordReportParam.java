package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AiKeParking2InRecordReportParam {
    @JsonProperty("record_number")//数据唯一标识
    private String id;

    /**
     * 卡号
     */
    @JsonProperty("enter_car_card_number")
    private String cardNo;

    /**
     * 车牌号码
     */
    @JsonProperty("car_license_number")
    private String vehicleNo;

    /**
     * 停车场编号
     */
    @JsonProperty("parking_lot_seq")
    private String parkingNo;

    /**
     * 停车场简称
     */
    @JsonProperty("parking_lot_name")
    private String parkingName;

    /**
     * 通道编号
     */
    @JsonProperty("enter_channel")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("enter_channel_name")
    private String passName;

    /**
     * 入场日期
     */
    @JsonProperty("enter_time")
    private String date;

    /**
     * 入场图片URL地址
     */
    @JsonProperty("enter_car_full_picture")
    private String inUrl;

    /**
     * 操作员
     */
    @JsonProperty("in_operator_name")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("enter_car_license_color")
    private Integer color;

    /**
     * 车辆类型
     */
    @JsonProperty("enter_car_type")
    private Integer vehicleType;

    private Integer tenant;
}
