package com.guhecloud.subsystem.requester.sdk.vo.drone;

import lombok.Data;

/**
 * 无人机任务VO
 */
@Data
public class DroneTaskVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务ID（唯一标识）
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务类型（标识任务的类型）
     */
    private Integer taskType;
    /**
     * 状态标识（任务状态）
     */
    private Integer status;
    /**
     * 备注信息（任务的补充说明）
     */
    private String remark;
    /**
     * 航线ID（关联的航线）
     */
    private Long trackId;
    /**
     * 航线名称
     */
    private String trackName;
    /**
     * 文件URL（航线文件访问地址）
     */
    private String fileUrl;
    /**
     * 机库序列号（执行任务的机库设备）
     */
    private String nestSn;
    /**
     * 机库名称
     */
    private String nestName;
    /**
     * 开始时间（计划开始时间戳）
     */
    private Long startTime;
    /**
     * 结束时间（计划结束时间戳）
     */
    private Long endTime;
    /**
     * 结果状态（任务执行结果状态）
     */
    private Integer resultStatus;
    /**
     * 天气冲突标识（是否因天气原因冲突）
     */
    private Boolean weatherConflict;
    /**
     * 起始航点索引（任务开始的航点位置）
     */
    private Integer startingPoint;
    /**
     * 最后一个航点索引（任务结束的航点位置）
     */
    private Integer lastPoint;
    /**
     * 执行时间（任务实际执行耗时，秒）
     */
    private Long executionTime;
    /**
     * 执行状态（任务当前的执行状态）
     */
    private Integer executionStatus;
}
