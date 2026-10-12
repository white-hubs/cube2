package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 采集市场数据配置
 * @date 2023-09-09 15:08:36
 */
@Data
public class SafetyAlarmRecord {

    //
    private Long id;
    //编号
    private String code;
    //级别主键
    private Long alarmLevelId;
    //级别编号
    private String alarmLevelCode;
    //级别名称
    private String alarmLevelName;
    //级别序号
    private Integer alarmLevelSort;
    //类型主键
    private Long alarmTypeId;
    //类型编号
    private String alarmTypeCode;
    //类型名称
    private String alarmTypeName;
    //类型图标附件
    private Long alarmTypeFileId;
    //对象主键
    private Long alarmTargetKey;
    //对象编号
    private String alarmTargetCode;
    //对象名称
    private String alarmTargetName;
    //对象类型主键
    private String alarmTargetTypeId;
    //对象类型名称
    private String alarmTargetTypeName;
    //对象经纬度信息
    private String alarmTargetLongitude;
    //对象位置信息
    private String alarmTargetAddress;
    //业务类型
    private String businessType;
    //业务主键
    private String businessKey;
    //报警发生时间
    private LocalDateTime alarmTime;
    //报警区域
    private String area;
    //报警区域名称
    private String areaName;
    //报警阀值
    private String alarmThreshold;
    //报警事件主键(报警规则id)
    private String alarmEvent;
    //报警事件名称
    private String alarmEventName;
    //事件类型ID
    private Long alarmEventTypeId;
    //事件类型编码
    private String alarmEventTypeCode;
    //事件类型名称
    private String alarmEventTypeName;
    //报警状态
    private String alarmStatus;
    //报警状态名称
    private String alarmStatusName;
    //状态序号
    private Integer alarmStatusOrder;
    //报警确认人员
    private String alarmConfirmedBy;
    //报警确认人员名称
    private String alarmConfirmedName;
    //报警确认时间
    private LocalDateTime alarmConfirmedTime;
    //报警确认结果描述
    private String alarmConfirmedResultRemark;
    //挂起时间
    private LocalDateTime suspendTime;
    //预计处理时间
    private LocalDateTime estimateHandlerTime;
    //报警处理人员
    private String alarmHandlerBy;
    //报警处理人员名称
    private String alarmHandlerName;
    //报警处理时间
    private LocalDateTime alarmHandlerTime;
    //报警处理结果状态
    private String alarmHandlerResult;
    //报警处理结果状态名称
    private String alarmHandlerResultName;
    //报警处理结果描述
    private String alarmHandlerResultRemark;
    //是否配置超时
    private Boolean timeoutEquipped;
    //超时处理时间
    private LocalDateTime timeoutProcessTime;
    //超时处理时间配置
    private Integer timeoutProcessTimes;
    //超时处理时间配置单位
    private String timeoutProcessTimesUnit;
    //超时时限(处理时间+超时处理时间单位)
    private String timeoutProcessTimesName;
    //处理耗时(单位秒)
    private Integer processingTime;
    //应用主键
    private Long applicationId;
    //应用编号
    private String applicationCode;
    //应用名称
    private String applicationName;
    //市场名称
    private String marketName;

    private Integer marketTenant;

    private Integer tenant;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Boolean deleted;

    /**
     * 是否显示
     */
    private Boolean displayed;

    /**
     * 辅助确认结果
     */
    private String assistConfirmResult;
}