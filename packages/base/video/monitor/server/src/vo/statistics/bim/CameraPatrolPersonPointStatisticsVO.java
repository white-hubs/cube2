package com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim;

import lombok.Data;

/**
 * 巡逻人员点位统计
 *
 * @author lanchun
 * @date 2024/8/29 21:02
 */
@Data
public class CameraPatrolPersonPointStatisticsVO {

    /**
     * 巡逻人员主键
     */
    private Long userId;

    /**
     * 巡逻人员名称
     */
    private String userName;

    /**
     * 巡逻点位总数
     */
    private Integer patrolCameraCount;
}
