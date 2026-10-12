package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmEventStatusStatisticsExportVO {

    /**
     * 报警事件名称
     */
    @ExcelProperty(value = { "报警事件" }, index = 0)
    private String alarmEventName;

    /**
     * 报警总数
     */
    @ExcelProperty(value = { "报警总数" }, index = 1)
    private Long totalNum;

    /**
     * 待接收报警数
     */
    @ExcelProperty(value = { "待接收报警数" }, index = 2)
    private Long unConfirmedNum;

    /**
     * 处理中报警数
     */
    @ExcelProperty(value = { "处理中报警数" }, index = 3)
    private Long progressConfirmedNum;

    /**
     * 已处理报警数
     */
    @ExcelProperty(value = { "已处理报警数" }, index = 4)
    private Long handlerNum;

    /**
     * 已挂起报警数
     */
    @ExcelProperty(value = { "已挂起报警数" }, index = 5)
    private Long suspendNum;

    /**
     * 超时报警数
     */
    @ExcelProperty(value = { "超时报警数" }, index = 6)
    private Long timeoutNum;
}
