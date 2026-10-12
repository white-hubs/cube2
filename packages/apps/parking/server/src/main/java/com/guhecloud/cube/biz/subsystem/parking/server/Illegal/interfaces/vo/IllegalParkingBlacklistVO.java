package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 11:09
 * @Description:
 */
@Data
public class IllegalParkingBlacklistVO {
    /**
     * id
     */
    private Long id;
    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车主
     */
    private String owner;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 加入原因
     */
    private String reason;

    /**
     * 加入时间
     */
    private LocalDateTime createTime;

    /**
     * 违停扣分
     */
    private BigDecimal violationScore;
}
