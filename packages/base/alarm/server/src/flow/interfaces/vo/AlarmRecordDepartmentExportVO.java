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
public class AlarmRecordDepartmentExportVO {

    /**
     * 部门名称
     */
    @ExcelProperty(value = { "部门名称" }, index = 0)
    private String departmentName;

    /**
     * 报警总数
     */
    @ExcelProperty(value = { "报警总数" }, index = 1)
    private Long alarmTotalNum;

    /**
     * 部门报警数
     */
    @ExcelProperty(value = { "部门报警数" }, index = 2)
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
     * 部门报警占比
     */
    @ExcelProperty(value = { "部门报警占比" }, index = 8)
    private String ratio;

    /**
     * 处理完成率
     */
    @ExcelProperty(value = { "报警处理率" }, index = 9)
    private String handlerRate;

    /**
     * 报警响应率
     */
    @ExcelProperty(value = { "报警响应率" }, index = 10)
    private String confirmedRate;
    /**
     * 报警超时率
     */
    @ExcelProperty(value = { "报警超时率" }, index = 11)
    private String timeoutRate;

    /**
     * 平均处理耗时
     */
    @ExcelProperty(value = { "处理平均耗时" }, index = 12)
    private String averageProcessing;

}
