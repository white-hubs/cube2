package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务计划表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_scheduled_task_plan")
public class MemberScheduledTaskPlan extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 定时任务id
     */
    private Long scheduledTaskId;
    /**
     * 指令：设备关闭、驱蚊指令、雾灌指令、定时任务等（ManualControlCommandEnum）
     */
    private ManualControlCommandEnum command;
    /**
     * 开始时间，格式 HH:mm
     */
    private String startTime;
    /**
     * 结束时间，格式 HH:mm
     */
    private String endTime;
    /**
     * 每周的第几天（1=周一 ~ 7=周日），定时任务模式为每周时使用
     */
    private Integer dayOfWeek;
}
