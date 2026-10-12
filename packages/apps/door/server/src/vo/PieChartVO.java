package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 饼状图实体
 */
@Data
public class PieChartVO {
    /**
     * 总数
     */
    private BigDecimal total;
    /**
     * 环比
     */
    private BigDecimal ringRatio;
    /**
     * 饼状图数据
     */
    private List<PieChartValueVO> data;
}
