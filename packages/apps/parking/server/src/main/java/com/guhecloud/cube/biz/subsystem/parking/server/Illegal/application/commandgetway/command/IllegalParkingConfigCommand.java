package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:00
 * @Description: 违停配置命令对象
 */
@Data
public class IllegalParkingConfigCommand {

    private Long id;

    /**
     * 统计周期
     * {@link com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalTimeCycleEnum}
     */
    private String timeCycle;

    /**
     * 违停程度
     */
    private IllegalLevelEnum illegalLevel;

    /**
     * 扣分值
     */
    private BigDecimal score;
}