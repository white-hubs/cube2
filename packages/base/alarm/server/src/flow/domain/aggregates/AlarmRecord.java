package com.guhecloud.cube.base.alarm.server.flow.domain.aggregates;

import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmInfo;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmLevel;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmType;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.Area;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.factory.AlarmRecordFactory;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmConfirmed;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmEventType;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmFilterScope;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandler;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmNotifyUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmPushConfig;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSnapshot;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmStatus;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSuspend;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTarget;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTimeoutProcess;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 报警记录
 *
 * @author lanchun
 * @date 2023/2/28 13:46
 */
@Getter
@Builder
@AllArgsConstructor
public class AlarmRecord {

    /**
     * 基本信息
     */
    private AlarmInfo alarmInfo;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 级别信息
     */
    private AlarmLevel alarmLevel;

    /**
     * 类型信息
     */
    private AlarmType alarmType;

    /**
     * 报警对象信息
     */
    private AlarmTarget alarmTarget;

    /**
     * 报警区域信息
     */
    private Area area;

    /**
     * 报警状态
     */
    private AlarmStatus alarmStatus;

    /**
     * 报警确认信息
     */
    private AlarmConfirmed alarmConfirmed;

    /**
     * 报警挂起信息
     */
    private AlarmSuspend alarmSuspend;

    /**
     * 报警事件分类信息
     */
    private AlarmEventType alarmEventType;

    /**
     * 报警处理信息
     */
    private AlarmHandler alarmHandler;

    /**
     * 报警超时信息
     */
    private AlarmTimeoutProcess alarmTimeoutProcess;

    /**
     * 报警快照信息
     */
    private AlarmSnapshot alarmSnapshot;

    /**
     * 报警作用域过滤
     */
    private AlarmFilterScope alarmFilterScope;

    /**
     * 处理用户
     */
    private List<AlarmHandlerUser> alarmHandlerUsers;

    /**
     * 报警通知人
     */
    private List<AlarmNotifyUser> alarmNotifyUsers;

    /**
     * 通知渠道配置
     */
    private List<String> channelTypes;

    /**
     * 推送配置
     */
    private AlarmPushConfig alarmPushConfig;

    /**
     * 规则ID
     */
    private Long ruleId;

    public AlarmRecord(AlarmInfo alarmInfo,
                       String businessType,
                       String businessKey,
                       AlarmLevel alarmLevel,
                       AlarmType alarmType,
                       AlarmTarget alarmTarget,
                       Area area,
                       AlarmStatus alarmStatus,
                       AlarmTimeoutProcess alarmTimeoutProcess,
                       AlarmSnapshot alarmSnapshot
                      ) {
        this.alarmInfo = alarmInfo;
        this.businessType = businessType;
        this.businessKey = businessKey;
        this.alarmLevel = alarmLevel;
        this.alarmType = alarmType;
        this.alarmTarget = alarmTarget;
        this.area = area;
        this.alarmStatus = alarmStatus;
        this.alarmTimeoutProcess = alarmTimeoutProcess;
        this.alarmSnapshot = alarmSnapshot;
    }

    public void changeAlarmStatus(AlarmStatusEnum alarmStatusEnum) {
        this.alarmStatus = new AlarmStatus(alarmStatusEnum, alarmStatusEnum.getOrder());
    }

    public void changeAlarmHandlerUsers(List<AlarmHandlerUser> alarmHandlerUsers) {
        this.alarmHandlerUsers = alarmHandlerUsers;
    }

    public void changeSingleHandlerUser(Long userId) {
        this.changeAlarmHandlerUsers(Collections.singletonList(AlarmRecordFactory.buildAlarmHandlerUser(userId)));
    }

    public void appendAlarmHandlerUsers(List<AlarmHandlerUser> alarmHandlerUsers) {
        if (CollectionUtils.isEmpty(this.alarmHandlerUsers)) {
            this.alarmHandlerUsers = alarmHandlerUsers;
        } else {
            alarmHandlerUsers.stream().forEach(alarmHandlerUser -> {
                if (this.alarmHandlerUsers.stream().noneMatch(user -> user.getId().equals(alarmHandlerUser.getId()))) {
                    this.alarmHandlerUsers.add(alarmHandlerUser);
                }
            });
        }
    }

    public void settingAlarmConfirmed(AlarmConfirmed alarmConfirmed) {
        this.alarmConfirmed = alarmConfirmed;
    }

    public void settingAlarmSuspend(AlarmSuspend alarmSuspend) {
        this.alarmSuspend = alarmSuspend;
    }

    public void settingAlarmHandler(AlarmHandler alarmHandler) {
        this.alarmHandler = alarmHandler;
    }

    public void checkHandlerUser(Long userId) {
        var isNotHandler = !CollectionUtils.isEmpty(this.alarmHandlerUsers) &&
                           this.alarmHandlerUsers.stream().noneMatch(alarmHandlerUser -> userId.equals(alarmHandlerUser.getId()));
        Assert.isPermissionDenied(isNotHandler, "操作失败,您不是指定的处理人");
    }

    public void settingChannelTypes(List<String> channelTypes) {
        this.channelTypes = channelTypes;
    }
}
