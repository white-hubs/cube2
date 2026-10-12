package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolTaskPageVO {
    private Long id;
    /**
     * 巡更任务名称
     */
    private String taskName;
    /**
     * 发布人
     */
    private String publisherName;
    /**
     * 发布人部门
     */
    private String publisherDepName;
    /**
     * 发布时间
     */
    private LocalDateTime publisherTime;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 巡更路线名称
     */
    private String patrolRouteName;
    /**
     * 巡更点
     */
    private Integer patrolLocationNum;
    /**
     * 巡更人id
     */
    private Long userId;
    /**
     * 巡更人
     */
    private String userName;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务情况
     */
    private String result;
    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 登录人是否任务归属人
     */
    private Boolean loginBelonger;

    /**
     * 协同人
     */
    private String assistIds;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;

    /**
     * 是否能分发
     */
    private Boolean distribution;

}
