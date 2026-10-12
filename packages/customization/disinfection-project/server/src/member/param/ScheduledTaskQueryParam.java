package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务分页查询入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduledTaskQueryParam extends Paging {
    /** 成员分组ID */
    private Long groupId;
    /** 任务名称（模糊） */
    private String name;
    /** 定时任务模式 */
    private ScheduleModeEnum scheduleMode;
}
