package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 定时任务创建入参：任务名称、定时任务模式、关联设备id列表、定时任务计划列表
 */
@Data
public class ScheduledTaskCreateParam {
    /** 成员分组ID */
    @NotNull(message = "成员分组ID不能为空")
    private Long groupId;
    /** 任务名称 */
    @NotNull(message = "任务名称不能为空")
    private String name;
    /** 定时任务模式：每天、每周 */
    @NotNull(message = "定时任务模式不能为空")
    private ScheduleModeEnum scheduleMode;
    /** 关联的设备id列表，可为空集合 */
    private List<Long> deviceIds;
    /** 定时任务计划列表（指令、执行开始时间、结束时间），可为空集合 */
    @Valid
    private List<ScheduledTaskPlanItemParam> planList;
}
