package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务设备表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_scheduled_task_device")
public class MemberScheduledTaskDevice extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 定时任务id
     */
    private Long scheduledTaskId;
}
