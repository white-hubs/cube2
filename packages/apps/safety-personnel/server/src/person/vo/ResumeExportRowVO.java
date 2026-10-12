package com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 安全履历导出行。
 */
@Data
public class ResumeExportRowVO {

    /**
     * 记录发生时间。
     */
    @ExcelProperty("时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(22)
    private LocalDateTime recordTime;

    /**
     * 任务名称。
     */
    @ExcelProperty("任务名称")
    @ColumnWidth(48)
    private String taskName;
}
