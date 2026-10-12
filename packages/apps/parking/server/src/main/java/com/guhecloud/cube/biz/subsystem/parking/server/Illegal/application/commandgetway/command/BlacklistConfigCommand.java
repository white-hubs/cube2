package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:48
 * @Description:
 */
@Data
public class BlacklistConfigCommand {

    private Long id;

    /**
     * 违停阈值
     */
    private BigDecimal thresholdValue;

    /**
     * 比较条件
     */
    private String comparisonCondition;

}
