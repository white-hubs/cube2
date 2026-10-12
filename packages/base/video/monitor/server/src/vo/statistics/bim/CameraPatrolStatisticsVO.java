package com.guhecloud.cube.biz.vmonitor.server.vo.statistics.bim;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraPatrolStatisticsVO {

    /**
     * 巡逻线路总数
     */
    private Integer patrolLineCount;

    /**
     * 巡逻点位数量
     */
    private Integer patrolCameraCount;

    /**
     * 巡逻人员数量
     */
    private Integer patrolUserCount;
}
