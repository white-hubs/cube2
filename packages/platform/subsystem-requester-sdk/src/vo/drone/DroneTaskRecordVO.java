package com.guhecloud.subsystem.requester.sdk.vo.drone;

import lombok.Data;

/**
 * 无人机任务执行记录VO
 */
@Data
public class DroneTaskRecordVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务ID（关联的任务）
     */
    private Long taskId;
    /**
     * 航线ID（关联的航线）
     */
    private Long trackId;
    /**
     * 计划ID（关联的执行计划）
     */
    private Long scheduledId;
    /**
     * 架次ID（单次飞行的架次标识）
     */
    private Long sortieId;
    /**
     * 机库序列号（执行任务的机库设备）
     */
    private String nestSn;
    /**
     * 机库名称
     */
    private String nestName;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 航线名称
     */
    private String trackName;
    /**
     * 开始时间（任务执行开始时间戳）
     */
    private Long startTime;
    /**
     * 结束时间（任务执行结束时间戳）
     */
    private Long endTime;
    /**
     * 结果状态（任务执行结果状态）
     */
    private Integer resultStatus;
    /**
     * 任务类型（标识任务的类型）
     */
    private Integer taskType;
    /**
     * 备注信息（任务执行的补充说明）
     */
    private String remark;
    /**
     * 架次编号（第几次飞行架次）
     */
    private Integer sortieNum;
    /**
     * 照片总数（计划拍摄的照片数量）
     */
    private Integer sumPhotoTotal;
    /**
     * 已完成照片数（实际拍摄完成的照片数量）
     */
    private Integer finishPhotoTotal;
    /**
     * 最后一个航点索引（当前执行到的航点位置）
     */
    private Integer lastPoint;
    /**
     * 状态标识（任务执行状态）
     */
    private Integer status;
    /**
     * 起始航点索引（任务开始的航点位置）
     */
    private Integer startingPoint;
    /**
     * 执行时间（任务实际执行耗时，秒）
     */
    private Long executionTime;
    /**
     * 执行状态（任务当前的执行状态）
     */
    private Integer executionStatus;
}
