package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author wql
 * @describe 海康入场出场放行事件公共参数
 * @date 2024/3/6 9:25
 */
@Data
public class HikParkingRecordParam {
    @JsonProperty("eventIndex")
    private String id;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 车牌号码
     */
    @JsonProperty("plateNo")
    private String vehicleNo;
    /**
     * 停车场编号
     */
    @JsonProperty("parkIndex")
    private String parkingNo;
    /**
     * 停车场简称
     */
    @JsonProperty("parkName")
    private String parkingName;
    /**
     * 出入口编号
     */
    @JsonProperty("gateIndex")
    private String passNo;

    /**
     * 出入口名称
     */
    @JsonProperty("gateName")
    private String passName;
    /**
     * 时间
     */
    private String time;

    /**
     * 车辆颜色
     */
    @JsonProperty("vehicleColor")
    private Integer color;

    /**
     * 车辆类型
     */
    private Integer vehicleType;

    /**
     * 车辆属性
     */
    private String carAttributeName;

    /**
     * 是否黑名单标识
     */
    private Integer alarmCar;

    /**
     * 事件号
     */
    private Integer eventCmd;
    /**
     * 进出场类型 0：进场，1：出场
     */
    private Integer inoutType;
    /**
     * 车辆主品牌
     */
    private Integer mainLogo;
    /**
     * 车牌置信度
     */
    private Integer plateBelieve;
    /**
     * 车牌颜色
     */
    private Integer plateColor;
    /**
     * 车牌类型
     */
    private String plateType;
    /**
     * 车道编号
     */
    private String roadwayIndex;
    /**
     * 车道名称
     */
    private String roadwayName;
    /**
     * 车道类型
     */
    private Integer roadwayType;
    /**
     * 车辆子品牌
     */
    private Integer subLogo;
    /**
     * 子品牌年款
     */
    private Integer subModel;
    /**
     * 车辆属性
     */
    private Integer vehicleClass;

    private Integer tenant;
}
