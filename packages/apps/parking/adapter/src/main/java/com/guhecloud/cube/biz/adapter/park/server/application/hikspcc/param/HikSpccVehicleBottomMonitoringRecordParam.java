package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xiaofeng
 * @describe 海康智慧监管综合平台出入 记录参数
 */
@Data
public class HikSpccVehicleBottomMonitoringRecordParam {

    /**
     * 车牌号
     */
    @JsonProperty("platNo")
    private String vehicleNo;

    /**
     * 来访事由名称
     */
    private String reason;

    /**
     * 来访事由编码
     */
    private String reasonType;

    /**
     * 记录编码
     */
    private String recordCode;

    /**
     * 出入标识
     */
    @JsonProperty("direction")
    private String direction;

    /**
     * 出入时间
     */
    private String captureTime;

    /**
     * 车底照片
     */
    @JsonProperty("underbodyPhoto")
    private String underBodyPhoto;

    /**
     * 车身抓拍照
     */
    private String bodyPhoto;

    /**
     * 车牌抓拍照
     */
    private String platePhoto;

    /**
     * 是否自动识别
     */
    private String isAuto;

    /**
     * 登记地点编码
     */
    private String orgCode;

    /**
     * 抓拍设备编码
     */
    private String deviceCode;

    /**
     * 抓拍设备类型
     */
    private String deviceType;

    /**
     * 租户编号
     */
    private Integer tenant;
}
