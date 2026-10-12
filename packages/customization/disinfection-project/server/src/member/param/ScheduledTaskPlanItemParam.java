package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 定时任务计划项：指令、执行开始时间、结束时间
 */
@Data
public class ScheduledTaskPlanItemParam {
    /**
     * 指令枚举
     */
    @NotNull(message = "指令不能为空")
    private ManualControlCommandEnum command;
    /**
     * 执行开始时间，格式 HH:mm
     */
    @NotNull(message = "执行开始时间不能为空")
    private String startTime;
    /**
     * 执行结束时间，格式 HH:mm
     */
    @NotNull(message = "执行结束时间不能为空")
    private String endTime;
    /**
     * 每周的第几天（1=周一 ~ 7=周日），定时任务模式为每周时使用
     */
    private Integer dayOfWeek;
}
