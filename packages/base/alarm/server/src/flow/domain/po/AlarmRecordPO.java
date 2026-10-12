package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 报警记录数据模型
 *
 * @author lanchun
 * @date 2022/6/6 10:24
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("alarm_record")
@DataPermission
public class AlarmRecordPO extends BasicEnhancedTenantEntity<Long> {

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
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

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
    private AlarmHandlerResultEnum alarmHandlerResult;

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
     * 物联快照
     */
    private String snapshot;

    /**
     * 触发报警信息
     */
    private String alarmSnapshot;

    /**
     * 业务快照
     */
    private String businessSnapshot;

    /**
     * 报警作用域
     */
    private AlarmFilterScopeEnum filterScope;

    /**
     * 规则ID
     */
    private Long ruleId;
}
