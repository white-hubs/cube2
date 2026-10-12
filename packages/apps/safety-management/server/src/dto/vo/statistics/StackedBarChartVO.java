package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 堆叠柱状图
 * @date 2023/9/8 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StackedBarChartVO {
    /**
     * x坐标值
     */
    private List<MarketBaseVO> xdata;
    /**
     * y坐标值
     */
    private List<PileUpMapVO> ydata;
}
