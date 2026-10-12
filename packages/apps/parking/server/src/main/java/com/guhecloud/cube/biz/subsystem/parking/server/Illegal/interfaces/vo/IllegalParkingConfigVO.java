package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:10
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IllegalParkingConfigVO {

    private Long id;

    /**
     * 统计周期
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
