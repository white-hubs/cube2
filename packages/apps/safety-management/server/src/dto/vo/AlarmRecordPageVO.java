package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 报警记录分页列表
 *
 * @author lanchun
 * @date 2023/9/8 14:29
 */
@Data
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmRecordPageVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 级别主键
     */
    private Long alarmLevelId;

    /**
     * 级别编号
     */
    private String alarmLevelCode;

    /**
     * 级别名称
     */
    @ExcelProperty(value = { "报警级别" }, index = 4)
    private String alarmLevelName;

    /**
     * 级别序号
     */
    private Integer alarmLevelSort;

    /**
     * 类型主键
     */
    private Long alarmTypeId;

    /**
     * 类型编号
     */
    private String alarmTypeCode;

    /**
     * 类型名称
     */
    private String alarmTypeName;

    /**
     * 对象主键
     */
    private Long alarmTargetKey;

    /**
     * 对象编号
     */
    private String alarmTargetCode;

    /**
     * 对象名称
     */
    private String alarmTargetName;

    /**
     * 对象类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 对象类型主键
     */
    private String alarmTargetTypeName;

    /**
     * 报警对象经纬度
     */
    private String alarmTargetLongitude;

    /**
     * 报警对象位置信息
     */
    private String alarmTargetAddress;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 报警发生时间
     */
    @ExcelProperty(value = { "报警时间" }, index = 3)
    private LocalDateTime alarmTime;

    /**
     * 报警区域
     */
    private String area;

    /**
     * 报警区域名称
     */
    @ExcelProperty(value = { "报警区域" }, index = 2)
    private String areaName;

    /**
     * 报警阀值
     */
    private String alarmThreshold;

    /**
     * 可能是报警事件或者能耗异常类型（用于指定此次报警是什么引起的）
     */
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    @ExcelProperty(value = { "报警事件" }, index = 0)
    private String alarmEventName;
    /**
     * 报警事件分类名称
     */
    private String alarmEventTypeName;
    /**
     * 报警事件分类主键
     */
    private Long alarmEventTypeId;

    /**
     * 报警事件分类code
     */
    private String alarmEventTypeCode;

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    @ExcelProperty(value = { "报警市场" }, index = 1)
    private String marketName;

    /**
     * 报警状态
     */
    private String alarmStatus;

    /**
     * 报警状态名称
     */
    @ExcelProperty(value = { "处理状态" }, index = 7)
    private String alarmStatusName;

    /**
     * 报警确认人员
     */
    private Long alarmConfirmedBy;

    /**
     * 报警确认人员名称
     */
    private String alarmConfirmedName;

    /**
     * 报警确认时间
     */
    private LocalDateTime alarmConfirmedTime;

    /**
     * 预计处理时间
     */
    @ExcelProperty(value = { "预计处理时间" }, index = 8)
    private LocalDateTime estimateHandlerTime;

    /**
     * 报警处理人员
     */
    private String alarmHandlerBy;

    /**
     * 报警处理人员名称
     */
    @ExcelProperty(value = { "处理人" }, index = 9)
    private String alarmHandlerName;

    /**
     * 报警处理时间
     */
    private LocalDateTime alarmHandlerTime;

    /**
     * 报警处理结果
     */
    private String alarmHandlerResult;

    /**
     * 报警处理结果
     */
    private String alarmHandlerResultName;

    /**
     * 报警处理结果描述
     */
    private String alarmHandlerResultRemark;

    /**
     * 是否配备超时
     */
    private Boolean timeoutEquipped = false;

    /**
     * 超时处理时间
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
    private String alarmHandlerTimeoutStatus;

    /**
     * 报警超时状态名称
     */
    @ExcelProperty(value = { "超时状态" }, index = 6)
    private String alarmHandlerTimeoutStatusName;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 报警确认结果
     */
    private String assistConfirmResult;

    public String getAlarmHandlerTimeoutStatus() {
        if (Objects.nonNull(this.timeoutEquipped) && this.timeoutEquipped && Objects.nonNull(this.timeoutProcessTime)) {
            var now = LocalDateTime.now();
            //超时处理
            boolean handlerTimeout = AlarmStatusEnum.ALARM_HANDLER.name().equals(this.alarmStatus) &&
                                     (timeoutProcessTime.isBefore(alarmHandlerTime) || timeoutProcessTime.isEqual(alarmHandlerTime));
            //未处理超时
            boolean unHandlerTimeOut = !AlarmStatusEnum.ALARM_HANDLER.name().equals(this.alarmStatus) &&
                                       (timeoutProcessTime.isBefore(now) || timeoutProcessTime.isEqual(now));
            if (handlerTimeout || unHandlerTimeOut) {
                return AlarmHandlerTimeoutStatusEnum.TIMED_OUT.name();
            }
        }
        return AlarmHandlerTimeoutStatusEnum.NORMAL.name();
    }

    public String getAlarmHandlerTimeoutStatusName() {
        return AlarmHandlerTimeoutStatusEnum.valueOf(getAlarmHandlerTimeoutStatus()).getDescription();
    }

    public String getAlarmHandlerName() {
        if (AlarmStatusEnum.ALARM_UN_CONFIRMED.name().equals(this.alarmStatus)) {
            return StringUtils.EMPTY_STRING;
        }
        return alarmHandlerName;
    }
}
