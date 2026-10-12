package com.guhecloud.cube.biz.adapter.park.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆出场缴费记录返回参数
 *
 * @author lanchun
 * @date 2022/9/28 17:45
 */
@Data
public class ParkingPayRecordVO {

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
     * 停车时长
     */
    private Integer parkingTimes;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实收金额
     */
    private BigDecimal freeAmount;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    private Integer tenant;
}
