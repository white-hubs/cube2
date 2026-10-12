package com.guhecloud.cube.biz.adapter.park.server.application.aike.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AiKeParking2OutRecordReportParam {
    @JsonProperty("record_number")
    private String id;

    /**
     * 卡号
     */
    @JsonProperty("leave_car_card_number")
    private String cardNo;

    /**
     * 车牌号码
     */
    @JsonProperty("leave_car_license_number")
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
    @JsonProperty("leave_channel")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("leave_channel_name")
    private String passName;

    /**
     * 出场日期
     */
    @JsonProperty("leave_time")
    private String date;

    /**
     * 出场图片URL地址
     */
    @JsonProperty("leave_car_full_picture")
    private String outUrl;

    /**
     * 实收金额
     */
    @JsonProperty("actual_receivable")
    private String parkingFee;

    /**
     * 优惠金额
     */
    @JsonProperty("discount_amount")
    private String parkingDiscFee;

    /**
     * 操作员
     */
    @JsonProperty("out_operator_name")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("leave_car_color")
    private Integer color;

    /**
     * 车辆类型
     */
    @JsonProperty("leave_car_type")
    private Integer vehicleType;

    private Integer tenant;
}
