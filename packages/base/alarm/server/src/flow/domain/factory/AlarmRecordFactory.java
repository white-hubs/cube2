package com.guhecloud.cube.base.alarm.server.flow.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.IncreaseAlarmRecordTargetCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmInfo;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmLevel;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmType;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.Area;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmEventType;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmFilterScope;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmNotifyUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmPushConfig;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSnapshot;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmStatus;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTarget;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTimeoutProcess;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.redis.util.RedisUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 报警记录创建工厂
 *
 * @author lanchun
 * @date 2023/2/28 15:39
 */
public class AlarmRecordFactory {

    public static AlarmRecord newAlarmRecord(final IncreaseAlarmRecordCommand increaseAlarmRecordCommand) {
        //基本信息
        AlarmInfo alarmInfo = new AlarmInfo(IdWorker.getId(),
                                            String.valueOf(RedisUtils.generateIdentity()),
                                            increaseAlarmRecordCommand.getAlarmEvent(),
                                            increaseAlarmRecordCommand.getAlarmEventName(),
                                            increaseAlarmRecordCommand.getAlarmThreshold(),
                                            increaseAlarmRecordCommand.getAlarmTime());
        //报警级别
        AlarmLevel alarmLevel = new AlarmLevel(increaseAlarmRecordCommand.getAlarmLevelId(), increaseAlarmRecordCommand
                .getAlarmLevelCode(), increaseAlarmRecordCommand.getAlarmLevelName(), increaseAlarmRecordCommand
                                                       .getAlarmLevelSort());
        //报警类型
        AlarmType alarmType = new AlarmType(increaseAlarmRecordCommand.getAlarmTypeId(), increaseAlarmRecordCommand
                .getAlarmTypeCode(), increaseAlarmRecordCommand.getAlarmTypeName(), increaseAlarmRecordCommand
                                                    .getApplicationId(), increaseAlarmRecordCommand
                                                    .getApplicationCode(), increaseAlarmRecordCommand.getApplicationName());
        //报警对象信息
        AlarmTarget alarmTarget = new AlarmTarget(increaseAlarmRecordCommand.getAlarmTargetKey(),
                                                  increaseAlarmRecordCommand.getAlarmTargetCode(),
                                                  increaseAlarmRecordCommand.getAlarmTargetName(),
                                                  increaseAlarmRecordCommand.getAlarmTargetTypeId(),
                                                  increaseAlarmRecordCommand.getAlarmTargetTypeName(),
                                                  increaseAlarmRecordCommand.getAlarmTargetLongitude(),
                                                  increaseAlarmRecordCommand.getAlarmTargetAddress());
        //区域信息
        Area area = new Area(increaseAlarmRecordCommand.getArea(), increaseAlarmRecordCommand.getAreaName());
        //报警状态
        AlarmStatus alarmStatus = new AlarmStatus(AlarmStatusEnum.ALARM_UN_CONFIRMED, AlarmStatusEnum.ALARM_UN_CONFIRMED
                .getOrder());
        //超时信息
        AlarmTimeoutProcess alarmTimeoutProcess = new AlarmTimeoutProcess(increaseAlarmRecordCommand
                                                                                  .getTimeoutProcessTimes(), increaseAlarmRecordCommand
                                                                                  .getTimeoutProcessTimesUnit());
        //报警快照信息
        AlarmSnapshot alarmSnapshot = new AlarmSnapshot(increaseAlarmRecordCommand.getSnapshot(), increaseAlarmRecordCommand
                .getAlarmSnapshot(), null);
        //报警事件类型信息
        AlarmEventType alarmEventType = new AlarmEventType(increaseAlarmRecordCommand
                                                                   .getAlarmEventTypeId(), increaseAlarmRecordCommand
                                                                   .getAlarmEventTypeCode(), increaseAlarmRecordCommand
                                                                   .getAlarmEventTypeName());
        //通知人信息
        List<AlarmNotifyUser> alarmNotifyUsers = new ArrayList<>();
        //处理用户信息
        List<AlarmHandlerUser> alarmHandlerUsers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(increaseAlarmRecordCommand.getHandlerUsers())) {
            //按用户id去重
            List<IncreaseAlarmRecordTargetCommand> increaseAlarmRecordTargetCommands = increaseAlarmRecordCommand
                    .getHandlerUsers().stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator
                                                                                                              .comparing(IncreaseAlarmRecordTargetCommand::getUserId))), ArrayList::new));
            increaseAlarmRecordTargetCommands.forEach(handlerUser -> {
                AlarmHandlerUser alarmHandlerUser = new AlarmHandlerUser(handlerUser.getUserId(), handlerUser
                        .getUserName(), handlerUser.getDepartmentId(), handlerUser.getDepartmentName());
                AlarmNotifyUser alarmNotifyUser = new AlarmNotifyUser(handlerUser.getUserId(), handlerUser
                        .getUserName(), handlerUser.getDepartmentId(), handlerUser.getDepartmentName());
                alarmHandlerUsers.add(alarmHandlerUser);
                alarmNotifyUsers.add(alarmNotifyUser);
            });
        }
        //报警作用域信息
        AlarmFilterScope alarmFilterScope = new AlarmFilterScope(increaseAlarmRecordCommand.getFilterScope());
        //报警推送配置
        AlarmPushConfig alarmPushConfig = new AlarmPushConfig(increaseAlarmRecordCommand.getFilterScope(), increaseAlarmRecordCommand.getTurnWarningSound());
        return AlarmRecord.builder()
                .alarmInfo(alarmInfo)
                .alarmEventType(alarmEventType)
                .businessKey(increaseAlarmRecordCommand.getBusinessKey())
                .businessType(increaseAlarmRecordCommand.getBusinessType())
                .alarmLevel(alarmLevel)
                .alarmType(alarmType)
                .alarmTarget(alarmTarget)
                .area(area)
                .alarmStatus(alarmStatus)
                .alarmTimeoutProcess(alarmTimeoutProcess)
                .alarmSnapshot(alarmSnapshot)
                .alarmHandlerUsers(alarmHandlerUsers)
                .alarmNotifyUsers(alarmNotifyUsers)
                .alarmFilterScope(alarmFilterScope)
                .channelTypes(increaseAlarmRecordCommand.getChannelTypes())
                .alarmPushConfig(alarmPushConfig)
                .ruleId(increaseAlarmRecordCommand.getRuleId())
                .build();
    }

    public static List<AlarmNotifyUser> buildAlarmNotifyUsers(List<IncreaseAlarmRecordTargetCommand> handlerUsers) {
        var increaseAlarmRecordTargetCommands = handlerUsers.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator
                                                                                                          .comparing(IncreaseAlarmRecordTargetCommand::getUserId))), ArrayList::new));
        return increaseAlarmRecordTargetCommands.stream().map(handlerUser ->
                                                                      new AlarmNotifyUser(handlerUser.getUserId(), handlerUser
                                                                              .getUserName(), handlerUser.getDepartmentId(), handlerUser.getDepartmentName())
                                                             ).toList();
    }

    public static List<AlarmHandlerUser> buildAlarmHandlerUsers(List<IncreaseAlarmRecordTargetCommand> handlerUsers) {
        //按用户id去重
        var increaseAlarmRecordTargetCommands = handlerUsers.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator
                                                                                                          .comparing(IncreaseAlarmRecordTargetCommand::getUserId))), ArrayList::new));
        return increaseAlarmRecordTargetCommands.stream().map(handlerUser ->
                                                                      new AlarmHandlerUser(handlerUser.getUserId(), handlerUser
                                                                              .getUserName(), handlerUser.getDepartmentId(), handlerUser.getDepartmentName())
                                                             ).toList();

    }

    public static AlarmHandlerUser buildAlarmHandlerUser(Long userId) {
        if (ObjectUtils.isEmpty(userId)) {
            throw new ResourcePreconditionFailedException("用户id不能为空");
        }
        UserClient userClient = ApplicationContextHolder.getBean(UserClient.class);
        UserDTO userDTO = userClient.getUser(userId);
        if (ObjectUtils.isEmpty(userDTO)) {
            throw new ResourceNotFoundException("未找到用户");
        }
        if (ObjectUtils.isEmpty(userDTO.getOrganization())) {
            return new AlarmHandlerUser(userDTO.getId(), userDTO.getName(), null, null);
        } else {
            return new AlarmHandlerUser(userDTO.getId(), userDTO.getName(), userDTO
                    .getOrganization().getId(), userDTO.getOrganization().getName());
        }
    }
}
