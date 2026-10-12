package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import lombok.Data;

/**
 * 线损趋势查询参数
 */
@Data
public class LineLossTrendParam {
    /**
     * 线路 ID
     */
    private Long lineId;

    /**
     * 时间范围类型：WEEK-本周，MONTH-本月，YEAR-今年
     */
    private String timeRange;
}
