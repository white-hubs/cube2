package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.plan;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PatrolPlanPageVO {
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 生效时间
     */
    private LocalDateTime startData;
    /**
     * 失效时间
     */
    private LocalDateTime endData;
    /**
     * 巡更路线
     */
    private Long routeId;
    private String routeName;
    /**
     * 巡更周期
     */
    private String patrolCycle;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 计划状态
     */
    private String status;
    /**
     * 巡更方式
     */
    private String patrolWay;
    /**
     * 任务时间范围
     */
    List<String> taskTimeRangeList;
}
