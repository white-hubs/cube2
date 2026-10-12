package com.guhecloud.cube.base.alarm.server.state;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.AlarmReasonConfigCommandService;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.AssignAlarmRecordUserCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.BatchHandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.DynamicAssignAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.HandlerAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.SuspendAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.TransferAlarmRecordCommand;
import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.IncreaseAlarmReasonConfigCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.AssignAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.ConfirmedAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.HandlerAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.SuspendAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.event.TransferAlarmRecordEvent;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.AlarmRecordRepository;
import com.guhecloud.cube.base.alarm.server.state.operate.AbstractAlarmStateOperateService;
import com.guhecloud.cube.base.alarm.server.state.operate.impl.HandlerAlarmOperateService;
import com.guhecloud.cube.base.alarm.server.state.operate.impl.ProgressAlarmOperateService;
import com.guhecloud.cube.base.alarm.server.state.operate.impl.SuspendAlarmOperateService;
import com.guhecloud.cube.base.alarm.server.state.operate.impl.UnConfirmedAlarmOperateService;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQueryService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 报警状态上下文
 *
 * @author lanchun
 * @date 2023/2/20 14:55
 */
@Slf4j
@Component
public class AlarmStateContext {

    @Autowired
    private AlarmRecordRepository alarmRecordRepository;

    @Autowired
    private AlarmReasonConfigCommandService alarmReasonConfigCommandService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserQueryService userQueryService;

    private final Map<AlarmStatusEnum, AbstractAlarmStateOperateService> alarmStateOperatorServiceMap = new EnumMap<>(AlarmStatusEnum.class);

    public AlarmStateContext() {
        //初始化报警状态对应操作服务
        alarmStateOperatorServiceMap.put(AlarmStatusEnum.ALARM_HANDLER, new HandlerAlarmOperateService(this));
        alarmStateOperatorServiceMap.put(AlarmStatusEnum.ALARM_PROGRESS_CONFIRMED, new ProgressAlarmOperateService(this));
        alarmStateOperatorServiceMap.put(AlarmStatusEnum.ALARM_SUSPEND, new SuspendAlarmOperateService(this));
        alarmStateOperatorServiceMap.put(AlarmStatusEnum.ALARM_UN_CONFIRMED, new UnConfirmedAlarmOperateService(this));
    }

    /**
     * 报警确认
     *
     * @param alarmRecord
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmedAlarmRecord(AlarmRecord alarmRecord) {
        //处理人验证
        alarmRecord.checkHandlerUser(SecurityContextHolder.getPrincipal().getId());
        //报警确认
        this.getAlarmState(alarmRecord).confirmedAlarmRecord(alarmRecord);
        //修改报警信息
        this.alarmRecordRepository.save(alarmRecord);
        //发送待接收状态变更事件
        this.applicationEventPublisher.publishEvent(new ConfirmedAlarmRecordEvent(this, alarmRecord));
    }

    /**
     * 报警移交
     *
     * @param alarmRecord
     * @param param
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferAlarmRecord(AlarmRecord alarmRecord, TransferAlarmRecordCommand param) {
        //处理人验证
        alarmRecord.checkHandlerUser(SecurityContextHolder.getPrincipal().getId());
        //获取当前状态
        AlarmStatusEnum currentStatus = alarmRecord.getAlarmStatus().getAlarmStatus();
        //报警移交
        this.getAlarmState(alarmRecord).transferAlarmRecord(alarmRecord, param);
        //修改报警信息
        this.alarmRecordRepository.save(alarmRecord);
        //发送状态变更事件
        this.applicationEventPublisher.publishEvent(new TransferAlarmRecordEvent(this, currentStatus, alarmRecord));
    }

    /**
     * 批量移交
     *
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfersAlarmRecord(List<TransferAlarmRecordCommand> params) {
        params.forEach(userModifyParam -> {
            AlarmRecord alarmRecord = this.alarmRecordRepository.detail(userModifyParam.getId());
            if (Objects.nonNull(alarmRecord)) {
                this.transferAlarmRecord(alarmRecord, userModifyParam);
            }
        });
    }

    /**
     * 报警指派
     *
     * @param alarmRecord
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignAlarmRecord(AlarmRecord alarmRecord, AssignAlarmRecordCommand param) {
        if (CollectionUtils.isEmpty(param.getAssignUsers())) {
            return;
        }
        //获取原先的处理人
        List<AlarmHandlerUser> alarmHandlerUsers = ObjectUtils.copying(alarmRecord.getAlarmHandlerUsers(), AlarmHandlerUser.class);
        //获取当前状态
        AlarmStatusEnum currentStatus = alarmRecord.getAlarmStatus().getAlarmStatus();
        //报警指派
        this.getAlarmState(alarmRecord).assignAlarmRecord(alarmRecord, param);
        //修改报警信息
        this.alarmRecordRepository.save(alarmRecord);
        //发送状态变更事件
        this.applicationEventPublisher.publishEvent(new AssignAlarmRecordEvent(this, currentStatus, alarmRecord, param, alarmHandlerUsers));
    }

    /**
     * 报警指派
     *
     * @param alarmRecord
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void dynamicAssignAlarmRecord(AlarmRecord alarmRecord, DynamicAssignAlarmRecordCommand param) {
        switch (param.getAssignUser().getTargetType()) {
            case DEPARTMENT -> this.userQueryService.organizationIds(param.getAssignUser().getTargetIds().stream().map(Long::valueOf)
                                                                             .collect(Collectors.toSet()));
            case ROLE -> this.userQueryService.roleIds(param.getAssignUser().getTargetIds().stream().map(Long::valueOf)
                                                               .collect(Collectors.toSet()));
            case USER -> this.userQueryService.userIds(param.getAssignUser().getTargetIds().stream().map(Long::valueOf)
                                                               .collect(Collectors.toSet()));
        }
        var userDTOS = this.userQueryService.enabled().postList();
        var assignUsers = userDTOS.stream().map(user -> {
            AssignAlarmRecordUserCommand command = new AssignAlarmRecordUserCommand();
            command.setUserId(user.getId());
            command.setUserName(user.getName());
            if (Objects.nonNull(user.getOrganization())) {
                command.setDepartmentId(user.getOrganization().getId());
                command.setDepartmentName(user.getOrganization().getName());
            }
            return command;
        }).collect(Collectors.toList());
        var assignParam = new AssignAlarmRecordCommand();
        assignParam.setId(param.getId());
        assignParam.setAssignUsers(assignUsers);
        assignParam.setCcMessageNotifies(param.getCcMessageNotifies());
        this.assignAlarmRecord(alarmRecord, assignParam);
    }

    /**
     * 报警挂起
     *
     * @param alarmRecord
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspendAlarmRecord(AlarmRecord alarmRecord, SuspendAlarmRecordCommand param) {
        //获取当前状态
        AlarmStatusEnum currentStatus = alarmRecord.getAlarmStatus().getAlarmStatus();
        //报警挂起
        this.getAlarmState(alarmRecord).suspendAlarmRecord(alarmRecord, param);
        //修改报警信息
        this.alarmRecordRepository.save(alarmRecord);
        //发送状态变更事件
        this.applicationEventPublisher.publishEvent(new SuspendAlarmRecordEvent(this, currentStatus, alarmRecord, param));
    }

    /**
     * 报警处理
     *
     * @param alarmRecord
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void handlerAlarmRecord(AlarmRecord alarmRecord, HandlerAlarmRecordCommand param) {
        //启用分布式锁,防止处理时重复操作
        RedissonRedLock alarmRecordHandlerLock = RedisUtils.getDistributedLock().tryLock(alarmRecord.getAlarmInfo().getId()
                                                                                                 .toString(), Expiration
                                                                                                 .from(5000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(alarmRecordHandlerLock)) {
                AlarmRecord detail = this.alarmRecordRepository.detail(alarmRecord.getAlarmInfo().getId());
                if (detail.getAlarmStatus().getAlarmStatus() == AlarmStatusEnum.ALARM_HANDLER) {
                    return;
                }
                if (Objects.nonNull(param.getAlarmReason()) && Objects.isNull(param.getAlarmReason().getId()) &&
                    StringUtils.isNotEmpty(param.getAlarmReason().getReason())) {
                    IncreaseAlarmReasonConfigCommand command = new IncreaseAlarmReasonConfigCommand();
                    command.setReason(param.getAlarmReason().getReason());
                    Long id = this.alarmReasonConfigCommandService.increaseAlarmReasonConfig(command);
                    param.getAlarmReason().setId(id);
                }
                //获取当前状态
                AlarmStatusEnum currentStatus = alarmRecord.getAlarmStatus().getAlarmStatus();
                //处理操作
                this.getAlarmState(alarmRecord).handlerAlarmRecord(alarmRecord, param);
                //修改报警信息
                this.alarmRecordRepository.save(alarmRecord);
                //发送状态变更事件
                this.applicationEventPublisher.publishEvent(new HandlerAlarmRecordEvent(this, currentStatus, alarmRecord, param));
            }
        } catch (Exception e) {
            log.error("处理报警失败,失败原因", e);
        } finally {
            if (Objects.nonNull(alarmRecordHandlerLock)) {
                alarmRecordHandlerLock.unlock();
            }
        }
    }

    /**
     * 批量处理
     *
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchHandlerAlarmRecords(@NonNull BatchHandlerAlarmRecordCommand param) {
        param.getIds().forEach(alarmRecordId -> {
            AlarmRecord alarmRecord = this.alarmRecordRepository.detail(alarmRecordId);
            if (Objects.nonNull(alarmRecord)) {
                this.handlerAlarmRecord(alarmRecord, HandlerAlarmRecordCommand.builder()
                        .handlerUser(param.getHandlerUser())
                        .alarmHandlerResult(param.getAlarmConfirmedResult())
                        .alarmHandlerResultRemark(param.getAlarmConfirmedResultRemark())
                        .build());
            }
        });
    }

    /**
     * 修改状态
     *
     * @param alarmRecord
     * @param newState
     */
    public void changeAlarmState(AlarmRecord alarmRecord, AlarmStatusEnum newState) {
        alarmRecord.changeAlarmStatus(newState);
    }

    private AbstractAlarmStateOperateService getAlarmState(AlarmRecord alarmRecord) {
        AbstractAlarmStateOperateService alarmStateOperateService = alarmStateOperatorServiceMap.get(alarmRecord.getAlarmStatus()
                                                                                                             .getAlarmStatus());
        if (Objects.nonNull(alarmStateOperateService)) {
            return alarmStateOperateService;
        }
        throw new UnsupportedOperationException("未知状态操作");
    }
}
