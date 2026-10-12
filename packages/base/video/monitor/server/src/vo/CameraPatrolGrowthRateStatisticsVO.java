package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2022/11/15 9:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolGrowthRateStatisticsVO {

    /**
     * 数量
     */
    private Integer totalNum;

    /**
     * 环比增长率
     */
    private String ringRatio;
}
