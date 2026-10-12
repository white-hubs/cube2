package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class InspectPlanStatisticsExportDTO {

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("巡检类型")
    private String typeName;

    @ExcelProperty("任务数")
    private Long taskTotal;

    @ExcelProperty("已完成任务数")
    private Long completedTaskTotal;

    @ExcelProperty("待完成任务数")
    private Long pendingTaskTotal;

    @ExcelProperty("对象总数")
    private Long objectTotal;

    @ExcelProperty("已巡检对象数")
    private Long inspectedObjectTotal;

    @ExcelProperty("待巡检对象数")
    private Long pendingObjectTotal;

    @ExcelProperty("异常巡检项数")
    private Long abnormalItemTotal;

    @ExcelProperty("未处理异常项数")
    private Long unhandledAbnormalItemTotal;

    @ExcelProperty("已处理异常项数")
    private Long handledAbnormalItemTotal;
}
