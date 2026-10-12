package com.guhecloud.cube.biz.adapter.park.server.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInRecordReportRequest {

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
     * 入场日期 YYYYMMDD
     */
    private String date;

    /**
     * 入场时间 hhmmss
     */
    private String time;

    /**
     * 入场图片URL地址
     */
    private String inUrl;

    /**
     * 入场图片(base64)
     */
    private String inImg;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 租户信息
     */
    private Integer tenant;
}
