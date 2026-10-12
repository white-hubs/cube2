package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 霞凝无人机任务实体
 * <p>
 * 对应数据库表 drone_task，存储从霞凝平台同步的任务数据。
 * 任务是无人机执行的具体工作单元，关联航迹、机巢等信息。
 * </p>
 *
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_task")
public class DroneTask extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID */
    private Long id;
    /** 霞凝平台的任务ID（第三方ID） */
    private Long thirdTaskId;
    /** 霞凝平台的航迹ID（第三方ID） */
    private Long thirdRouteId;
    /** 任务名称 */
    private String taskName;
    /** 航迹名称 */
    private String trackName;
    /** 机巢序列号 */
    private String nestSn;
    /** 机巢名称 */
    private String nestName;
    /** 航迹文件URL */
    private String fileUrl;
    /** 任务类型 */
    private Integer taskType;
    /** 任务状态 */
    private Integer taskStatus;
    /** 结果状态 */
    private Integer resultStatus;
    /** 是否天气冲突 */
    private Boolean weatherConflict;
    /** 起始航点 */
    private Integer startingPoint;
    /** 结束航点 */
    private Integer lastPoint;
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
