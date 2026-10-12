package com.guhecloud.subsystem.requester.sdk.vo.parking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆出场记录返回参数
 *
 * @author lanchun
 * @date 2022/9/28 17:43
 */
@Data
public class ParkingOutRecordVO {

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
