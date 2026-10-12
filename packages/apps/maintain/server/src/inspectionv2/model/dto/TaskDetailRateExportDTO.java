package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检任务详情巡检项列表导出
 */
@Data
public class TaskDetailRateExportDTO {

    @ExcelProperty("任务名称")
    @ColumnWidth(32)
    private String taskName;

    @ExcelProperty("对象名称")
    @ColumnWidth(24)
    private String targetName;

    @ExcelProperty("巡检项名称")
    @ColumnWidth(36)
    private String itemName;

    @ExcelProperty("巡检结果")
    @ColumnWidth(14)
    private String result;

    @ExcelProperty("巡检人")
    @ColumnWidth(16)
    private String inspectorName;

    @ExcelProperty("巡检时间")
    @ColumnWidth(22)
    private LocalDateTime inspectionTime;

    @ExcelProperty("巡检备注")
    @ColumnWidth(36)
    private String remark;
}
