package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:03
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlacklistConfigVO {

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
