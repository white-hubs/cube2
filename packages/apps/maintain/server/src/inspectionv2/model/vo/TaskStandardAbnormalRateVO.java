package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务标准项异常率
 */
@Data
public class TaskStandardAbnormalRateVO {

    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 执行人
     */
    private String nickname;

    /**
     * 执行时间
     */
    private LocalDateTime updateTime;

    /**
     * 任务标准名称
     */
    private String standardName;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 标准项个数
     */
    private Long totalItemCount;

    /**
     * 异常数
     */
    private Long abnormalItemCount;

    /**
     * 异常率
     */
    private BigDecimal abnormalRate;
}
