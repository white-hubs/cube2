package com.guhecloud.cube.base.alarm.client.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警发生消息
 *
 * @author codex
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRecordOccurredMessage {

    public static final String TOPIC = "ALARM_RECORD_OCCURRED";

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

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
     * 报警阈值
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
    private String filterScope;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 通知渠道
     */
    private List<String> channelTypes;

    /**
     * 是否开启警告声音
     */
    private Boolean turnWarningSound;

    /**
     * 处理用户
     */
    private List<UserMessage> handlerUsers;

    /**
     * 报警通知人
     */
    private List<UserMessage> notifyUsers;

    /**
     * 是否已删除
     */
    private Boolean deleted;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新用户
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMessage {

        private Long userId;

        private String userName;

        private Long departmentId;

        private String departmentName;
    }
}
