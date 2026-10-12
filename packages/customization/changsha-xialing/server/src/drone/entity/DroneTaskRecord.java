package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 霞凝无人机任务记录实体
 * <p>
 * 对应数据库表 drone_task_record，存储从霞凝平台同步的任务执行记录数据。
 * 任务记录是任务的单次执行实例，一个任务可以有多次执行记录（如多次重试）。
 * </p>
 *
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_task_record")
public class DroneTaskRecord extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID */
    private Long id;
    /** 霞凝平台的任务记录ID（第三方ID） */
    private Long thirdRecordId;
    /** 霞凝平台的任务ID（第三方ID） */
    private Long thirdTaskId;
    /** 霞凝平台的航迹ID（第三方ID） */
    private Long thirdRouteId;
    /** 霞凝平台的调度ID（第三方ID），用于关联成果数据 */
    private Long thirdScheduleId;
    /** 架次ID */
    private Long sortieId;
    /** 任务名称 */
    private String taskName;
    /** 航迹名称 */
    private String trackName;
    /** 机巢序列号 */
    private String nestSn;
    /** 机巢名称 */
    private String nestName;
    /** 任务类型 */
    private Integer taskType;
    /** 调度状态 */
    private Integer scheduleStatus;
    /** 结果状态 */
    private Integer resultStatus;
    /** 起始航点 */
    private Integer startingPoint;
    /** 结束航点 */
    private Integer lastPoint;
    /** 架次编号 */
    private Integer sortieNum;
    /** 照片总数 */
    private Integer sumPhotoTotal;
    /** 已完成照片数 */
    private Integer finishPhotoTotal;
    /** 执行时间 */
    private LocalDateTime executionTime;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
    /** 备注 */
    private String remark;
    /** 原始JSON数据，用于调试和追溯 */
    private String rawJson;
    /** 最后一次同步时间 */
    private LocalDateTime lastSyncTime;
}
