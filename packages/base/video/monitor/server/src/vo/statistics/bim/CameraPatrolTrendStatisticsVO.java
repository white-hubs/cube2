package com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡逻异常趋势统计
 *
 * @author lanchun
 * @date 2024/8/29 19:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolTrendStatisticsVO {

    /**
     * 数量
     */
    private Integer totalNum;

    /**
     * 时间(等同于横坐标)
     */
    private String period;
}
