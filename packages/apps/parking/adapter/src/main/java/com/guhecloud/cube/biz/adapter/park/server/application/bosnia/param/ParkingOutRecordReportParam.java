package com.guhecloud.cube.biz.adapter.park.server.application.bosnia.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 博西尼入场记录上报参数
 *
 * @author lanchun
 * @date 2022/10/9 9:27
 */
@Data
public class ParkingOutRecordReportParam {

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
    @JsonProperty("vehicleno")
    private String vehicleNo;

    /**
     * 车辆模式
     */
    @JsonProperty("vehiclemode")
    private Integer vehicleMode;

    /**
     * 停车场编号
     */
    @JsonProperty("parkingno")
    private String parkingNo;

    /**
     * 停车场简称
     */
    @JsonProperty("parkingname")
    private String parkingName;

    /**
     * 通道编号
     */
    @JsonProperty("passno")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("passname")
    private String passName;

    /**
     * 出场日期
     */
    @JsonProperty("date")
    private String date;

    /**
     * 出场时间
     */
    @JsonProperty("time")
    private String time;

    /**
     * 出场图片URL地址
     */
    @JsonProperty("outurl")
    private String outUrl;

    /**
     * 出场图片(base64)
     */
    @JsonProperty("outimg")
    private String outImg;

    /**
     * 停车时间
     */
    @JsonProperty("parkingtimes")
    private Integer parkingTimes;

    /**
     * 实收金额
     */
    @JsonProperty("parkingfee")
    private String parkingFee;

    /**
     * 优惠金额
     */
    @JsonProperty("parkingdiscfee")
    private String parkingDiscFee;

    /**
     * 有效日期
     */
    @JsonProperty("validdate")
    private String validDate;

    /**
     * 操作员
     */
    @JsonProperty("operator")
    private String operator;

    /**
     * 车辆颜色
     */
    @JsonProperty("color")
    private Integer color;

    /**
     * 车辆类型
     */
    @JsonProperty("vehicletype")
    private Integer vehicleType;

    private Integer tenant;
}
