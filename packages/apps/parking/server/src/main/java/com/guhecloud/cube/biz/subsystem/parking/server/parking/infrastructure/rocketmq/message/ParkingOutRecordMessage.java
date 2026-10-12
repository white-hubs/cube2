package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.rocketmq.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 出场记录消息
 *
 * @author lanchun
 * @date 2022/10/14 14:55
 */
@Data
public class ParkingOutRecordMessage {

    public final static String TOPIC = "PARKING_OUT_RECORD";

    /**
     * 同一辆车一次出入的序列号一样
     */
    private String identifyId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 车场号
     */
    private String parkingNo;

    /**
     * 车场名称
     */
    private String parkingName;

    /**
     * 出场通道号
     */
    private String channelNo;

    /**
     * 出场通道名称
     */
    private String channelName;

    /**
     * 出场时间
     */
    private LocalDateTime outTime;

    /**
     * 出场图片路径
     */
    private String outImageUrl;

    /**
     * 出场图片
     */
    private String outImageBase;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 车场编号
     */
    private String parkingInfoCode;

    /**
     * 车场名称
     */
    private String parkingInfoName;
}
