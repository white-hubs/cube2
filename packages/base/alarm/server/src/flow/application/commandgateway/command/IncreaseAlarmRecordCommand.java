package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmFilterScopeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警消息发送参数
 *
 * @author lanchun
 * @date 2022/6/6 11:11
 */
@Data
public class IncreaseAlarmRecordCommand {

    private String id;

    /**
     * 级别主键
     */
    @NotNull(message = "级别主键不能为空")
    @Min(value = 1, message = "级别主键不能小于1")
    private Long alarmLevelId;

    /**
     * 级别编号
     */
    @NotEmpty(message = "级别编号不能为空")
    @Size(max = 50, message = "级别编号不能大于50")
    private String alarmLevelCode;

    /**
     * 级别名称
     */
    @NotEmpty(message = "级别名称不能为空")
    @Size(max = 50, message = "级别名称不能大于50")
    private String alarmLevelName;

    /**
     * 级别序号
     */
    @NotNull(message = "级别序号不能为空")
    @Min(value = 1, message = "级别序号不能小于1")
    private Integer alarmLevelSort;

    /**
     * 类型主键
     */
    @NotNull(message = "类型主键不能为空")
    @Min(value = 1, message = "类型主键不能小于1")
    private Long alarmTypeId;

    /**
     * 类型编号
     */
    @NotEmpty(message = "类型编号不能为空")
    @Size(max = 50, message = "类型编号不能大于50")
    private String alarmTypeCode;

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称不能为空")
    @Size(max = 50, message = "类型名称不能大于50")
    private String alarmTypeName;

    /**
     * 类型图标附件主键
     */
    private Long alarmTypeFileId;

    /**
     * 对象主键
     */
    @NotNull(message = "对象主键不能为空")
    @Min(value = 1, message = "对象主键不能小于1")
    private Long alarmTargetKey;

    /**
     * 对象编号
     */
    @NotEmpty(message = "对象编号不能为空")
    @Size(max = 50, message = "对象编号不能大于50")
    private String alarmTargetCode;

    /**
     * 对象名称
     */
    @NotEmpty(message = "对象名称不能为空")
    @Size(max = 50, message = "对象名称不能大于50")
    private String alarmTargetName;

    /**
     * 对象类型主键
     */
    @NotNull(message = "对象类型主键不能为空")
    @Min(value = 1, message = "对象类型主键不能小于1")
    private String alarmTargetTypeId;

    /**
     * 对象类型名称
     */
    @NotEmpty(message = "对象类型名称不能为空")
    @Size(max = 50, message = "对象类型名称不能大于50")
    private String alarmTargetTypeName;

    /**
     * 报警对象经纬度
     */
    @Size(max = 100, message = "对象经纬度不能大于100")
    private String alarmTargetLongitude;

    /**
     * 对象位置信息
     */
    @Size(max = 250, message = "对象位置信息不能大于250")
    private String alarmTargetAddress;

    /**
     * 业务类型
     */
    @NotEmpty(message = "业务类型不能为空")
    @Size(max = 50, message = "业务类型不能大于50")
    private String businessType;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空")
    @Min(value = 1, message = "业务主键不能小于1")
    private String businessKey;

    /**
     * 报警发生时间
     */
    @NotNull(message = "报警发生事件不能为空")
    private LocalDateTime alarmTime;

    /**
     * 报警区域
     */
    @Size(max = 200, message = "报警区域不能大于200")
    private String area;

    /**
     * 报警区域名称
     */
    @Size(max = 50, message = "报警区域名称不能大于50")
    private String areaName;

    /**
     * 报警阀值
     */
    @Size(max = 50, message = "报警阀值不能大于50")
    private String alarmThreshold;

    /**
     * 可能是报警事件或者能耗异常类型（用于指定此次报警是什么引起的）
     */
    @NotEmpty(message = "报警事件不能为空")
    @Size(max = 50, message = "报警事件不能大于50")
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    @NotEmpty(message = "报警事件名称不能为空")
    @Size(max = 100, message = "报警事件名称不能大于100")
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
    @Size(max = 100, message = "预计处理时间单位长度不能大于100")
    private String timeoutProcessTimesUnit;

    /**
     * 应用主键
     */
    @NotNull(message = "应用主键不能为空")
    private Long applicationId;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    @Size(max = 50, message = "应用编号不能大于50")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @Size(max = 50, message = "应用名称不能大于50")
    private String applicationName;

    /**
     * 报警的报警快照
     */
    private String snapshot;

    /**
     * 触发报警快照
     */
    private String alarmSnapshot;

    /**
     * 报警作用域
     */
    private AlarmFilterScopeEnum filterScope;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;

    /**
     * 处理用户
     */
    @NotNull(message = "处理用户不能为空")
    @Size(min = 1, message = "处理用户不能为空")
    private List<IncreaseAlarmRecordTargetCommand> handlerUsers;

    /**
     * 通知渠道配置
     */
    private List<String> channelTypes;

    /**
     * 规则ID
     */
    private Long ruleId;
}
