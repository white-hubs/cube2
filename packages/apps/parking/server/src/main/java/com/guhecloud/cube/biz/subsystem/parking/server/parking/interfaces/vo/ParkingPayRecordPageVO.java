package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费记录分页返回参数
 *
 * @author lanchun
 * @date 2022/10/13 13:42
 */
@Data
public class ParkingPayRecordPageVO {

    private Long id;

    /**
     * 通行唯一标识
     */
    private String identifyId;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 停车时长
     */
    private Integer parkingTimes;

    /**
     * 收费方式
     */
    private String payType;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 实收金额
     */
    private BigDecimal freeAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 入场时间
     */
    private LocalDateTime accessInTime;

    /**
     * 车场编号
     */
    private String parkingInfoCode;

    /**
     * 车场名称
     */
    private String parkingInfoName;
}
