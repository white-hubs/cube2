package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result;

import lombok.Data;

/**
 * 趋势返回对象
 */
@Data
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
