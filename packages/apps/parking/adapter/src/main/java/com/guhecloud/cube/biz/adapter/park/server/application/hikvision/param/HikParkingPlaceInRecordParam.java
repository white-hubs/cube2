package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 海康车位停入记录
 *
 * @author lanchun
 * @date 2024/12/17 11:15
 */
@Data
public class HikParkingPlaceInRecordParam {

    /**
     * 车位相机id
     */
    private String cameraId;

    /**
     * 车位相机syscode
     */
    @JsonProperty("cameraSyscode")
    private String cameraSysCode;

    /**
     * 事件syscode
     */
    @JsonProperty("eventSyscode")
    private String eventSysCode;

    /**
     * 事件时间
     */
    private String eventTime;

    /**
     * 事件类型 0:离开,1:停入
     */
    private Integer eventType;

    /**
     * 楼层ID
     */
    private String floorId;

    /**
     * 楼层syscode
     */
    @JsonProperty("floorSyscode")
    private String floorSysCode;

    /**
     * 停车库ID
     */
    private String parkingId;

    /**
     * 停车库syscode
     */
    @JsonProperty("parkingSyscode")
    private String parkingSysCode;

    /**
     * 车位图片
     */
    private String picUrl;

    /**
     * 车位ID
     */
    private String placeId;

    /**
     * 车位syscode
     */
    @JsonProperty("placeSyscode")
    private String placeSysCode;

    /**
     * 图片服务器编号
     */
    private String plateNo;

    /**
     * 诱导管理器ID
     */
    private String svrIndex;

    /**
     * 诱导管理器ID
     */
    private String tpmId;
}
