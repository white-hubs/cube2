package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

/**
 * 线路统计分页返回参数
 *
 * @author lanchun
 * @date 2022/11/9 11:26
 */
@Data
public class CameraPatrolLineStatisticsPageVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 监控数量
     */
    private Integer cameraNum;

    /**
     * 巡逻次数
     */
    private Integer patrolNum;

    /**
     * 巡逻人员名称
     */
    private String patrolUserNames;

    /**
     * 巡逻异常数量
     */
    private Integer abnormalNum;
}
