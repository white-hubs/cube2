package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.DateUnitEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 报警记录导出返回参数
 *
 * @author lanchun
 * @date 2022/6/7 10:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmRecordExportVO {

    private Long id;

    /**
     * 报警发生时间
     */
    @ExcelProperty(value = { "报警时间" }, index = 0)
    private LocalDateTime alarmTime;

    /**
     * 对象名称
     */
    @ExcelProperty(value = { "报警设备" }, index = 1)
    private String alarmTargetName;

    /**
     * 级别名称
     */
    @ExcelProperty(value = { "报警级别" }, index = 2)
    private String alarmLevelName;

    /**
     * 报警事件名称
     */
    @ExcelProperty(value = { "报警事件" }, index = 3)
    private String alarmEventName;

    /**
     * 报警区域名称
     */
    @ExcelProperty(value = { "报警区域" }, index = 4)
    private String areaName;

    /**
     * 是否配备超时
     */
    private Boolean timeoutEquipped;

    /**
     * 超时时间
     */
    private LocalDateTime timeoutProcessTime;

    /**
     * 超时处理时间
     */
    private Integer timeoutProcessTimes;

    /**
     * 超时处理时间单位
     */
    private String timeoutProcessTimesUnit;

    /**
     * 超时时限(处理事件+超时处理时间单位)
     */
    @ExcelProperty(value = { "处理时限" }, index = 5)
    private String timeoutProcessTimesName;

    /**
     * 报警超时状态
     */
    private AlarmHandlerTimeoutStatusEnum alarmHandlerTimeoutStatus;

    /**
     * 报警超时状态名称
     */
    @ExcelProperty(value = { "超时状态" }, index = 6)
    private String alarmHandlerTimeoutStatusName;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 报警状态名称
     */
    @ExcelProperty(value = { "处理状态" }, index = 7)
    private String alarmStatusName;

    /**
     * 预计处理时间
     */
    @ExcelProperty(value = { "预计处理时间" }, index = 8)
    private LocalDateTime estimateHandlerTime;

    public String getAlarmHandlerName() {
        //待接收状态不显示处理人信息
        if (AlarmStatusEnum.ALARM_UN_CONFIRMED == alarmStatus) {
            return StringUtils.EMPTY_STRING;
        }
        return alarmHandlerName;
    }

    /**
     * 报警确认人员名称
     */
    @ExcelProperty(value = { "处理人" }, index = 9)
    private String alarmHandlerName;

    public String getTimeoutProcessTimesName() {
        if (Objects.nonNull(this.timeoutProcessTimes) && 0 < this.timeoutProcessTimes) {
            return this.timeoutProcessTimes + "" + DateUnitEnum.valueOf(this.timeoutProcessTimesUnit).getDescription();
        }
        return null;
    }

    public String getAlarmHandlerTimeoutStatusName() {
        if (Objects.nonNull(this.timeoutEquipped) && this.timeoutEquipped
            && Objects.nonNull(this.timeoutProcessTime)
            && this.timeoutProcessTime.isBefore(DateUtils.now())) {
            return AlarmHandlerTimeoutStatusEnum.TIMED_OUT.getDescription();
        }
        return AlarmHandlerTimeoutStatusEnum.NORMAL.getDescription();
    }

    public String getAlarmStatusName() {
        return this.alarmStatus.getDescription();
    }
}
