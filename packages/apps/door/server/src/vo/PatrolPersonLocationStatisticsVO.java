package com.guhecloud.cube.biz.door.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡更人员最新位置统计
 * @date 2024/3/15 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatrolPersonLocationStatisticsVO {
    /**
     * 时间
     */
    private String datetime;
    /**
     * 人员
     */
    private String personName;
    /**
     * 点位
     */
    private String location;

}
