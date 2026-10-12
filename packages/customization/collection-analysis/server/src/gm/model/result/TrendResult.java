package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 趋势返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendResult {
    /**
     * 趋势x轴数据(一般是时间)
     */
    private String trendX;
    /**
     * 趋势y轴
     */
    private String trendY;
}
