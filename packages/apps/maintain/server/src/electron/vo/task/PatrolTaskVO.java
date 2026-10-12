package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolTaskVO {
    /**
     * 任务id
     */
    private Long id;
    /**
     * 巡更任务名称
     */
    private String taskName;
    /**
     * 任务开始时间
     */
    private LocalDateTime startDate;
    /**
     * 任务结束时间
     */
    private LocalDateTime endDate;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 发布人
     */
    private String publisherName;
    /**
     * 发布部门
     */
    private String publisherDepName;
    /**
     * 巡更点数
     */
    private Integer patrolLocationNum;
    /**
     * 已巡点数
     */
    private Long completedNum;
    /**
     * 未巡点数
     */
    private Long undoneNum;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务完成时间
     */
    private LocalDateTime completeDate;
    /**
     * 巡更路线名称
     */
    private String routeName;
    /**
     * 是否按照顺序执行
     */
    private Boolean inOrder;
    /**
     * 巡更人id
     */
    private Long userId;
    /**
     * 巡更人员
     */
    private String userName;
    /**
     * 巡更部门
     */
    private String userDep;
    /**
     * 确认状态
     */
    private Boolean affirmState;
    /**
     * 巡更方式
     */
    private List<String> patrolWayList;
    /**
     * 巡更明细
     */
    private List<PatrolTaskDataVO> data;
    /**
     * 巡更任务时段
     */
    private List<String> executePeriod;
    /**
     * 正常的巡更点数
     */
    private Long normalLocationNum;
    /**
     * 异常的巡更点数
     */
    private Long untowardLocationNum;
    /**
     * 巡更任务时段
     */
    private List<PatrolTaskPeriodVO> executePeriodList;
    /**
     * 登录人是否任务归属人
     */
    private Boolean loginBelonger;
    /**
     * 是否可以分发
     */
    private Boolean distribution;

    /**
     * 协同人
     */
    private String assistIds;
    private String assistNames;
}
