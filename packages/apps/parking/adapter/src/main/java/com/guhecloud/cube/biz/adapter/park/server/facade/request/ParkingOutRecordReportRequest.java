package com.guhecloud.cube.biz.adapter.park.server.facade.request;

import lombok.Data;

@Data
public class ParkingOutRecordReportRequest {

    private String id;

    /**
     * 人员编号
     */
    private String perNo;

    /**
     * 人员姓名
     */
    private String perName;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 车牌号码
     */
    private String vehicleNo;

    /**
     * 车牌颜色
     */
    private String plateColor;

    /**
     * 车辆类型
     */
    private Integer vehicleType;

    /**
     * 车辆颜色
     */
    private Integer color;

    /**
     * 车辆模式
     */
    private Integer vehicleMode;

    /**
     * 停车场编号
     */
    private String parkingNo;

    /**
     * 停车场简称
     */
    private String parkingName;

    /**
     * 通道编号
     */
    private String passNo;

    /**
     * 通道简称
     */
    private String passName;

    /**
     * 出场日期 YYYYMMDD
     */
    private String date;

    /**
     * 出场时间 hhmmss
     */
    private String time;

    /**
     * 出场图片URL地址
     */
    private String outUrl;

    /**
     * 出场图片(base64)
     */
    private String outImg;

    /**
     * 停车时间
     */
    private Integer parkingTimes;

    /**
     * 实收金额
     */
    private String parkingFee;

    /**
     * 优惠金额
     */
    private String parkingDiscFee;

    /**
     * 有效日期
     */
    private String validDate;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 租户信息
     */
    private Integer tenant;

    /**
     * 计费结束时间
     */
    private String calcEndTime;
}
