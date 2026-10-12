package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 巡更点top统计
 * @date 2024/1/24 18:25
 */
@Data
public class LocationTopStatisticsVO {
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 巡更点名称
     */
    private String locationName;
    /**
     * 异常/漏巡次数
     */
    private Integer num;
    /**
     * 环比
     */
    private Float chainRatio;
}
