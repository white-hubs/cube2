package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 任务标准项异常率导出
 */
@Data
public class TaskStandardAbnormalRateExportDTO {

    @ExcelProperty("对象名称")
    private String targetName;

    @ExcelProperty("执行人")
    private String nickname;

    @ExcelProperty("执行时间")
    private LocalDateTime updateTime;

    @ExcelProperty("任务标准名称")
    private String standardName;

    @ExcelProperty("任务名称")
    private String taskName;

    @ExcelProperty("标准项个数")
    private Long totalItemCount;

    @ExcelProperty("异常数")
    private Long abnormalItemCount;

    @ExcelProperty("异常率(%)")
    private BigDecimal abnormalRate;
}
