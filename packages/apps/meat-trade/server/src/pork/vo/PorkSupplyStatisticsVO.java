package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 供货重量与片数统计
 *
 * @author lanchun
 * @date 2023/8/21 17:13
 */
@Data
public class PorkSupplyStatisticsVO {

    /**
     * 供货重量
     */
    private BigDecimal weight;

    /**
     * 供货片数
     */
    private Integer slices;
}
