package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检任务分页导出
 */
@Data
public class TaskPageExportDTO {

    @ExcelProperty("任务名称")
    private String taskName;

    @ExcelProperty("巡检类型")
    private String typeName;

    @ExcelProperty("任务类型")
    private String taskType;

    @ExcelProperty("执行人")
    private String executorName;

    @ExcelProperty("发布人")
    private String publisherName;

    @ExcelProperty("发布时间")
    private LocalDateTime publishTime;

    @ExcelProperty("截止时间")
    private LocalDateTime validEndTime;

    @ExcelProperty("巡检对象数")
    private Integer targetNum;

    @ExcelProperty("巡检进度")
    private String inspectProgress;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("异常对象数")
    private Integer errCount;

    @ExcelProperty("已修复对象数")
    private Integer repairCount;
}
