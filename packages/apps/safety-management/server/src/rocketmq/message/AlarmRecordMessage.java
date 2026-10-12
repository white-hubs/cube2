package com.guhecloud.cube.srv.gen.safety.management.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmRecordMessage {

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
     * 类型图标附件主键
     */
    private Long alarmTypeFileId;

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
     * 对象类型名称
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
    private LocalDateTime alarmTime;

    /**
     * 报警区域
     */
    private String area;

    /**
     * 报警区域名称
     */
    private String areaName;

    /**
     * 报警阀值
     */
    private String alarmThreshold;

    /**
     * 报警事件
     */
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警事件分类主键
     */
    private Long alarmEventTypeId;

    /**
     * 报警事件分类编号
     */
    private String alarmEventTypeCode;

    /**
     * 报警事件分类名称
     */
    private String alarmEventTypeName;

    /**
     * 报警状态
     */
    private String alarmStatus;

    /**
     * 报警状态名称
     */
    private String alarmStatusName;

    /**
     * 报警状态序号
     */
    private Integer alarmStatusOrder;

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
     * 挂起时间
     */
    private LocalDateTime suspendTime;

    /**
     * 预计处理时间
     */
    private LocalDateTime estimateHandlerTime;

    /**
     * 报警处理人员
     */
    private String alarmHandlerBy;

    /**
     * 报警处理人员名称
     */
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
     * 报警处理结果名称
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
    private String timeoutProcessTimesName;

    /**
     * 处理耗时
     */
    private long processingTime;

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
     * 市场编号
     */
    private Integer tenant;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 是否显示
     */
    private Boolean displayed;
}
