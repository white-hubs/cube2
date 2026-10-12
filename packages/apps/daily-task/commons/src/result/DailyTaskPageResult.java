package com.guhecloud.cube.biz.daily.task.commons.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DailyTaskPageResult {
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 发布人
     */
    private String publishUser;

    /**
     * 发布部门
     */
    private String publishDepartment;
    /**
     * 发布时间
     */
    private String startTime;

    /**
     * 任务类型
     */
    private String taskCategoryName;

    /**
     * 紧急程度
     */
    private String urgencyName;

    /**
     * 负责人
     */
    private String taskUserName;
    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务截至日期(任务的期限)
     */
    private LocalDateTime deadline;

    /**
     * 任务进度
     */
    private BigDecimal progress;

    /**
     * 任务对象数
     */
    private Integer targetCount;

    /**
     * 执行结束时间(任务完成的时间)
     */
    private LocalDateTime endTime;

    /**
     * 主任务
     */
    private Boolean master;

    /**
     * 是否是持续性任务
     */
    private Boolean continuity;

    /**
     * 是否异常任务
     */
    private Boolean abnormal;

    /**
     * 是否允许移交
     */
    private Boolean allowTransfer;
}
