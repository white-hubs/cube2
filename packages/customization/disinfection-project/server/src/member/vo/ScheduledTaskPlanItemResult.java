package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import lombok.Data;

/**
 * 定时任务计划项（详情返回）
 */
@Data
public class ScheduledTaskPlanItemResult {
    private ManualControlCommandEnum command;
    /** 开始时间，格式 HH:mm */
    private String startTime;
    /** 结束时间，格式 HH:mm */
    private String endTime;
    private Integer dayOfWeek;
}
