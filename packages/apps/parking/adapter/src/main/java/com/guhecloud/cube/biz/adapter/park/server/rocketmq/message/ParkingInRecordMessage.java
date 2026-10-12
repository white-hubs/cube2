package com.guhecloud.cube.biz.adapter.park.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingInRecordMessage {

    /**
     * 一条数据的唯一标识
     */
    private Long id;

    /**
     * 同一辆车一次出入的序列号一样
     */
    private String identifyId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 车牌颜色
     */
    private String plateColor;

    /**
     * 车场号
     */
    private String parkingNo;

    /**
     * 车场名称
     */
    private String parkingName;

    /**
     * 入场通道号
     */
    private String channelNo;

    /**
     * 入场通道名称
     */
    private String channelName;

    /**
     * 入场时间
     */
    private LocalDateTime inTime;

    /**
     * 入场图片路径
     */
    private String inImageUrl;

    /**
     * 入场图片
     */
    private String inImageBase;

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
