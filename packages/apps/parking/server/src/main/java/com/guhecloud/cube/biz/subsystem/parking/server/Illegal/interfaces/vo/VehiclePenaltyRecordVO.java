package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 15:28
 * @Description:
 */
@Data
public class VehiclePenaltyRecordVO {

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车主电话
     */
    private String ownerPhone;

    /**
     * 车主姓名
     */
    private String ownerName;
    /**
     * 违停时间
     */
    private LocalDateTime illegalTime;

    /**
     * 违停工单ID
     */
    private String processInstanceId;

    /**
     * 工单名称
     */
    private String worKOrderName;

    /**
     * 违停扣分
     */
    private BigDecimal violationScore;
}
