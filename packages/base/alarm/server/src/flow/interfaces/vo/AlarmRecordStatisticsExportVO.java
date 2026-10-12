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
public class AlarmRecordStatisticsExportVO {

    /**
     * 报警事件类型
     */
    @ExcelProperty(value = { "报警事件" }, index = 0)
    private String alarmEventName;

    /**
     * 报警总数
     */
    @ExcelProperty(value = { "报警总数" }, index = 1)
    private Long alarmTotalNum;

    /**
     * 事件报警数
     */
    @ExcelProperty(value = { "事件报警数" }, index = 2)
    private Long totalNum;

    /**
     * 未响应报警数
     */
    @ExcelProperty(value = { "未响应报警数" }, index = 3)
    private Long unconfirmedNum;

    /**
     * 处理中报警数
     */
    @ExcelProperty(value = { "处理中报警数" }, index = 4)
    private Long progressConfirmedNum;

    /**
     * 已处理报警数
     */
    @ExcelProperty(value = { "已处理报警数" }, index = 5)
    private Long handlerNum;

    /**
     * 已挂起报警数
     */
    @ExcelProperty(value = { "已挂起报警数" }, index = 6)
    private Long suspendNum;

    /**
     * 超时数量
     */
    @ExcelProperty(value = { "超时报警数" }, index = 7)
    private Long timeoutNum;
    /**
     * 真实报警数
     */
    @ExcelProperty(value = { "真实报警数" }, index = 8)
    private Long alarmActualNum;

    /**
     * 误报数
     */
    @ExcelProperty(value = { "误报数" }, index = 9)
    private Long alarmImpracticalNum;

    /**
     * 报警占比
     */
    @ExcelProperty(value = { "事件报警占比" }, index = 10)
    private String ratio;

    /**
     * 报警正确率
     */
    @ExcelProperty(value = { "报警正确率" }, index = 11)
    private String correctRate;

    /**
     * 处理完成率
     */
    @ExcelProperty(value = { "报警处理率" }, index = 12)
    private String handlerRate;

    /**
     * 报警响应率
     */
    @ExcelProperty(value = { "报警响应率" }, index = 13)
    private String confirmedRate;
    /**
     * 报警超时率
     */
    @ExcelProperty(value = { "报警超时率" }, index = 14)
    private String timeoutRate;

    /**
     * 平均处理耗时
     */
    @ExcelProperty(value = { "处理平均耗时" }, index = 15)
    private String averageProcessing;
}
