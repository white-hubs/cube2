package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:43
 * @Description:
 */
@Data
public class IllegalParkingBlacklistCommand {

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;

    /**
     * 车牌颜色
     */
    @NotBlank(message = "车牌颜色不能为空")
    private String plateColor;

    /**
     * 车主
     */
    @NotBlank(message = "车主不能为空")
    private String owner;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 加入原因
     */
    private String reason;

    /**
     * 时间周期
     */
    private String cycle;
    /**
     * 车场编号
     */
    private String parkingLot;

}
