package com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim;

import lombok.Data;

/**
 * 巡逻点位统计
 *
 * @author lanchun
 * @date 2024/8/29 20:45
 */
@Data
public class CameraPatrolPointStatisticsVO {

    /**
     * 巡逻总数
     */
    private Integer totalNum;

    /**
     * 监控主键
     */
    private Long id;

    /**
     * 监控名称
     */
    private String name;
}
