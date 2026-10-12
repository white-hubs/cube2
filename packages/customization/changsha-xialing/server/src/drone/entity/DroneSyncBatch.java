package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 霞凝无人机同步批次实体
 * <p>
 * 对应数据库表 drone_sync_batch，记录每次数据同步的执行情况。
 * 用于追踪航迹、任务、任务记录、成果等数据的同步状态和统计信息。
 * </p>
 *
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_sync_batch")
public class DroneSyncBatch extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID */
    private Long id;
    /** 同步域（route-航迹, task-任务, taskRecord-任务记录, result-成果） */
    private String syncDomain;
    /** 同步状态（0-进行中, 1-成功, 2-失败） */
    private Integer syncStatus;
    /** 总记录数 */
    private Integer totalCount;
    /** 成功记录数 */
    private Integer successCount;
    /** 失败记录数 */
    private Integer failCount;
    /** 错误信息 */
    private String errorMessage;
    /** 批次开始时间 */
    private LocalDateTime batchStartTime;
    /** 批次结束时间 */
    private LocalDateTime batchFinishTime;
}
