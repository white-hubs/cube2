package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

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
public class TrendResultVO {
    /**
     * 趋势x轴数据(一般是时间)
     */
    private String trendX;
    /**
     * 趋势y轴
     */
    private String trendY;
}
