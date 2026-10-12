package com.guhecloud.cube.base.alarm.client.dto;

import com.guhecloud.cube.base.alarm.client.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.base.alarm.client.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警记录返回参数
 *
 * @author lanchun
 * @date 2022/6/7 10:22
 */
@Data
public class AlarmRecordDTO {

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
     * 可能是报警事件或者能耗异常类型（用于指定此次报警是什么引起的）
     */
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 报警状态名称
     */
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
    private AlarmHandlerResultEnum alarmHandlerResult;

    /**
     * 报警处理结果
     */
    private String alarmHandlerResultName;

    /**
     * 报警处理结果描述
     */
    private String alarmHandlerResultRemark;

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
     * 报警超时状态
     */
    private AlarmHandlerTimeoutStatusEnum alarmHandlerTimeoutStatus;

    /**
     * a
     * 报警超时状态名称
     */
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
     * 报警快照
     */
    private String snapshot;

    /**
     * 类型图标附件信息
     */
    private FileVO fileVO;

    /**
     * 处理结果附件列表
     */
    private List<FileVO> handlerFileVOS;

    /**
     * 跳转地址
     */
    private String url;
}
