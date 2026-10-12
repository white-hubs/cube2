package com.guhecloud.cube.biz.adapter.park.server.application.dahua.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chx
 * @describe 大华车辆事件入参对象
 * @date 2025/4/15 17:14
 */
@Data
public class DaHuaParkingRecordParam {

    @JsonProperty("bizSn")
    private String id;

    /**
     * 人员编号
     */
    @JsonProperty("uid")
    private String perNo;

    /**
     * 人员姓名
     */
    private String perName;

    /**
     * 卡号
     */
    @JsonProperty("voucherNo")
    private String cardNo;

    /**
     * 车牌号码
     */
    @JsonProperty("carNum")
    private String vehicleNo;

    /**
     * 车牌颜色
     * 0-蓝 1-黄2-白3-黑 99-未识别
     */
    @JsonProperty("carNumcolor")
    private String plateColor;

    /**
     * 车辆类型
     */
    @JsonProperty("parkingCarType")
    private Integer vehicleType;

    /**
     * 车辆颜色
     */
    @JsonProperty("parkingCarColor")
    private Integer color;

    /**
     * 车辆模式
     * 0代表月租或长期车辆， 1代表时租访客(储值、临时)车辆， 2代表免费车辆， 3代表异常未知车辆）
     */
    @JsonProperty("actType")
    private Integer vehicleMode;

    /**
     * 停车场编号
     */
    @JsonProperty("parkingLot")
    private String parkingNo;

    /**
     * 停车场简称
     */
    private String parkingName;

    /**
     * 通道编号
     */
    @JsonProperty("sluiceDevChnId")
    private String passNo;

    /**
     * 通道简称
     */
    @JsonProperty("sluiceDevChnName")
    private String passName;

    /**
     * 出/入场日期 YYYYMMDD
     */
    private String date;

    /**
     * 出/入场时间 hhmmss
     */
    private String time;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 租户信息
     */
    private Integer tenant;

}
