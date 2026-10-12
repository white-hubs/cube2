package com.guhecloud.cube.biz.daily.task.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DailyPlanCommandParam extends DailyBaseParam {
    private Long id;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 执行方式 DailyExecuteWayEnum
     * (多种以逗号隔开,空表示可以直接点击执行)
     */
    private String executeWay;
    /**
     * 表达式id
     */
    private Long cronId;
    /**
     * 指定类型 JobLoopTypeEnum
     */
    private String specifiedType;
    /**
     * 指定的日期(根据指定的类型 这里可能是 月/周/号 等)
     * 例如 specifiedDays的值为7时: specifiedType为MONTH(每月)这里就是7月; specifiedType为WEEK时这里就是周日
     */
    private String specifiedDays;
    /**
     * 指定的时间(这里只有HH:mm:ss)
     */
    private String specifiedTime;

    /**
     * 附加字段数据
     */
    private String metadata;

}
