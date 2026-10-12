package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务信息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_scheduled_task")
public class MemberScheduledTask extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 成员分组ID
     */
    private Long groupId;
    /**
     * 定时任务名称
     */
    private String name;
    /**
     * 定时任务模式：每天、每周
     */
    private ScheduleModeEnum scheduleMode;
}
