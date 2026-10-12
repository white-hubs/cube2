package com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordHandlerUserService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordNotifyChannelService;
import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordPushConfigService;
import com.guhecloud.cube.base.alarm.server.flow.domain.aggregates.AlarmRecord;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmInfo;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmLevel;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.AlarmType;
import com.guhecloud.cube.base.alarm.server.flow.domain.entity.Area;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerReasonPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordHandlerUserPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordNotifyChannelPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordPushConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmConfirmed;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmEventType;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmFilterScope;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandler;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmPushConfig;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmReason;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSnapshot;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmStatus;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmSuspend;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTarget;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmTimeoutProcess;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordHandlerReasonMapper;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordHandlerUserMapper;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordMapper;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordRepository {

    private final AlarmRecordMapper alarmRecordMapper;

    private final AlarmRecordHandlerUserMapper alarmRecordHandlerUserMapper;

    private final AlarmRecordHandlerReasonMapper alarmRecordHandlerReasonMapper;

    private final AlarmRecordHandlerUserService alarmRecordHandlerUserService;

    private final AlarmRecordNotifyChannelService alarmRecordNotifyChannelService;

    private final AlarmRecordPushConfigService alarmRecordPushConfigService;

    /**
     * 保存报警记录
     *
     * @param alarmRecord
     *
     * @return
     */
    public AlarmRecord save(AlarmRecord alarmRecord) {
        boolean isExecuteSave = true;
        AlarmRecordPO existAlarmRecordPO = this.alarmRecordMapper.selectById(alarmRecord.getAlarmInfo().getId());
        if (Objects.nonNull(existAlarmRecordPO)) {
            isExecuteSave = false;
        }
        AlarmRecordPO alarmRecordPO = buildAlarmRecordPO(alarmRecord);
        if (Objects.nonNull(alarmRecord.getAlarmConfirmed())) {
            alarmRecordPO.setAlarmConfirmedBy(alarmRecord.getAlarmConfirmed().getAlarmConfirmedBy());
            alarmRecordPO.setAlarmConfirmedName(alarmRecord.getAlarmConfirmed().getAlarmConfirmedName());
            alarmRecordPO.setAlarmConfirmedTime(alarmRecord.getAlarmConfirmed().getAlarmConfirmedTime());
        }
        if (Objects.nonNull(alarmRecord.getAlarmSuspend())) {
            alarmRecordPO.setSuspendTime(alarmRecord.getAlarmSuspend().getSuspendTime());
            alarmRecordPO.setEstimateHandlerTime(alarmRecord.getAlarmSuspend().getEstimateHandlerTime());
        }
        if (Objects.nonNull(alarmRecord.getAlarmHandler())) {
            alarmRecordPO.setAlarmHandlerBy(alarmRecord.getAlarmHandler().getAlarmHandlerBy());
            alarmRecordPO.setAlarmHandlerName(alarmRecord.getAlarmHandler().getAlarmHandlerName());
            alarmRecordPO.setAlarmHandlerResult(alarmRecord.getAlarmHandler().getAlarmHandlerResult());
            alarmRecordPO.setAlarmHandlerResultRemark(alarmRecord.getAlarmHandler().getAlarmHandlerResultRemark());
            alarmRecordPO.setAlarmHandlerTime(alarmRecord.getAlarmHandler().getAlarmHandlerTime());
            alarmRecordPO.setProcessingTime(alarmRecord.getAlarmHandler().getProcessingTime());
            //报警处理原因
            if (Objects.nonNull(alarmRecord.getAlarmHandler().getAlarmHandlerReason())) {
                AlarmRecordHandlerReasonPO alarmRecordHandlerReasonPO = getAlarmRecordHandlerReasonPO(alarmRecord, alarmRecordPO);
                this.alarmRecordHandlerReasonMapper.insert(alarmRecordHandlerReasonPO);
            }
        }
        if (Objects.nonNull(existAlarmRecordPO)) {
            alarmRecordPO.setBusinessSnapshot(existAlarmRecordPO.getBusinessSnapshot());
            EntityAuditor.populateCreateAudit(alarmRecordPO, existAlarmRecordPO.getCreateBy(), existAlarmRecordPO.getCreatorId(),
                                              existAlarmRecordPO.getCreatorType(), existAlarmRecordPO.getCreateTime());
        }
        saveAlarmNotifyChannelTypes(alarmRecord);
        //保存报警推送配置
        if (Objects.nonNull(alarmRecord.getAlarmPushConfig()) &&
            Objects.nonNull(alarmRecord.getAlarmPushConfig().getTurnWarningSound())) {
            this.alarmRecordPushConfigService.removeByAlarmRecordId(alarmRecordPO.getId());
            AlarmRecordPushConfigPO recordPushConfig = AlarmRecordPushConfigPO.builder()
                    .alarmRecordId(alarmRecordPO.getId())
                    .turnWarningSound(alarmRecord.getAlarmPushConfig().getTurnWarningSound())
                    .build();
            this.alarmRecordPushConfigService.save(recordPushConfig);
        }
        //先删除处理人信息
        this.alarmRecordHandlerUserService.removeByAlarmRecordId(alarmRecordPO.getId(), null);
        saveHandlerUsers(alarmRecord);
        if (isExecuteSave) {
            this.alarmRecordMapper.insert(alarmRecordPO);
        } else {
            this.alarmRecordMapper.updateById(alarmRecordPO);
        }
        return alarmRecord;
    }

    public void saveHandlerUsers(AlarmRecord alarmRecord) {
        //保存处理人信息
        List<AlarmRecordHandlerUserPO> alarmRecordHandlerUserPOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(alarmRecord.getAlarmHandlerUsers())) {
            alarmRecord.getAlarmHandlerUsers()
                    .forEach(handlerUser -> {
                        var userPO = AlarmRecordHandlerUserPO.builder()
                                .id(IdWorker.getId())
                                .alarmRecordId(alarmRecord.getAlarmInfo().getId())
                                .userId(handlerUser.getId())
                                .userName(handlerUser.getUserName())
                                .departmentId(handlerUser.getDepartmentId())
                                .departmentName(handlerUser.getDepartmentName())
                                .tenant(SessionContextHolder.getRequester().getTenant())
                                .build();
                        EntityAuditor.populateAudit(userPO);
                        alarmRecordHandlerUserPOS.add(userPO);
                    });
            this.alarmRecordHandlerUserMapper.insertOrUpdateBatch(alarmRecordHandlerUserPOS);
        }
    }

    public void saveAlarmNotifyChannelTypes(AlarmRecord alarmRecord) {
        //保存报警通知渠道配置
        if (CollectionUtils.isNotEmpty(alarmRecord.getChannelTypes())) {
            this.alarmRecordNotifyChannelService.removeByAlarmRecordId(alarmRecord.getAlarmInfo().getId());
            AlarmRecordNotifyChannelPO alarmRecordNotifyChannel = AlarmRecordNotifyChannelPO.builder()
                    .alarmRecordId(alarmRecord.getAlarmInfo().getId())
                    .channelTypes(String.join(",", alarmRecord.getChannelTypes()))
                    .build();
            this.alarmRecordNotifyChannelService.save(alarmRecordNotifyChannel);
        }
    }

    private static AlarmRecordHandlerReasonPO getAlarmRecordHandlerReasonPO(AlarmRecord alarmRecord, AlarmRecordPO alarmRecordPO) {
        AlarmRecordHandlerReasonPO alarmRecordHandlerReasonPO = new AlarmRecordHandlerReasonPO();
        alarmRecordHandlerReasonPO.setAlarmRecordId(alarmRecordPO.getId());
        alarmRecordHandlerReasonPO.setAlarmReasonId(alarmRecord.getAlarmHandler().getAlarmHandlerReason().getId());
        alarmRecordHandlerReasonPO.setAlarmReasonCode(alarmRecord.getAlarmHandler().getAlarmHandlerReason().getCode());
        alarmRecordHandlerReasonPO.setAlarmReason(alarmRecord.getAlarmHandler().getAlarmHandlerReason().getReason());
        alarmRecordHandlerReasonPO.setAlarmReasonRemark(alarmRecord.getAlarmHandler().getAlarmHandlerReason()
                                                                .getRemark());
        return alarmRecordHandlerReasonPO;
    }

    private static AlarmRecordPO buildAlarmRecordPO(AlarmRecord alarmRecord) {
        //报警信息
        return AlarmRecordPO.builder()
                .id(alarmRecord.getAlarmInfo().getId())
                .code(alarmRecord.getAlarmInfo().getCode())
                .alarmThreshold(alarmRecord.getAlarmInfo().getAlarmThreshold())
                .alarmEvent(alarmRecord.getAlarmInfo().getAlarmEvent())
                .alarmEventName(alarmRecord.getAlarmInfo().getAlarmEventName())
                .alarmEventTypeId(alarmRecord.getAlarmEventType().getAlarmEventTypeId())
                .alarmEventTypeCode(alarmRecord.getAlarmEventType().getAlarmEventTypeCode())
                .alarmEventTypeName(alarmRecord.getAlarmEventType().getAlarmEventTypeName())
                .alarmTime(alarmRecord.getAlarmInfo().getAlarmTime())
                .alarmLevelId(alarmRecord.getAlarmLevel().getId())
                .alarmLevelCode(alarmRecord.getAlarmLevel().getCode())
                .alarmLevelName(alarmRecord.getAlarmLevel().getName())
                .alarmLevelSort(alarmRecord.getAlarmLevel().getSort())
                .alarmTypeId(alarmRecord.getAlarmType().getId())
                .alarmTypeCode(alarmRecord.getAlarmType().getCode())
                .alarmTypeName(alarmRecord.getAlarmType().getName())
                .alarmTargetKey(alarmRecord.getAlarmTarget().getAlarmTargetKey())
                .alarmTargetCode(alarmRecord.getAlarmTarget().getAlarmTargetCode())
                .alarmTargetName(alarmRecord.getAlarmTarget().getAlarmTargetName())
                .alarmTargetTypeId(alarmRecord.getAlarmTarget().getAlarmTargetTypeId())
                .alarmTargetTypeName(alarmRecord.getAlarmTarget().getAlarmTargetTypeName())
                .alarmTargetLongitude(alarmRecord.getAlarmTarget().getAlarmTargetLongitude())
                .alarmTargetAddress(alarmRecord.getAlarmTarget().getAlarmTargetAddress())
                .businessType(alarmRecord.getBusinessType())
                .businessKey(alarmRecord.getBusinessKey())
                .area(alarmRecord.getArea().getArea())
                .areaName(alarmRecord.getArea().getAreaName())
                .alarmStatus(alarmRecord.getAlarmStatus().getAlarmStatus())
                .alarmStatusOrder(alarmRecord.getAlarmStatus().getAlarmStatusOrder())
                .filterScope(alarmRecord.getAlarmFilterScope().getFilterScope())
                .timeoutEquipped(alarmRecord.getAlarmTimeoutProcess().getTimeoutEquipped())
                .timeoutProcessTime(alarmRecord.getAlarmTimeoutProcess()
                                            .getTimeoutProcessTime())
                .timeoutProcessTimes(alarmRecord.getAlarmTimeoutProcess()
                                             .getTimeoutProcessTimes())
                .timeoutProcessTimesUnit(alarmRecord.getAlarmTimeoutProcess()
                                                 .getTimeoutProcessTimesUnit())
                .applicationId(alarmRecord.getAlarmType().getApplicationId())
                .applicationCode(alarmRecord.getAlarmType().getApplicationCode())
                .applicationName(alarmRecord.getAlarmType().getApplicationName())
                .snapshot(alarmRecord.getAlarmSnapshot().getSnapshot())
                .alarmSnapshot(alarmRecord.getAlarmSnapshot().getAlarmSnapshot())
                .ruleId(alarmRecord.getRuleId())
                .deleted(false)
                .build();
    }

    /**
     * 设置报警记录的业务快照
     *
     * @param id
     * @param businessSnapshot
     */
    public void settingAlarmRecordBusinessSnapshot(Long id, String businessSnapshot) {
        this.alarmRecordMapper.settingAlarmRecordBusinessSnapshot(id, businessSnapshot);
    }

    /**
     * 报警记录详情
     *
     * @param id
     *
     * @return
     */
    public AlarmRecord detail(Long id) {
        AlarmRecordPO alarmRecordPO = this.alarmRecordMapper.selectById(id);
        if (Objects.isNull(alarmRecordPO)) {
            return null;
        }
        //报警处理人信息
        List<AlarmRecordHandlerUserPO> userPOS = this.alarmRecordHandlerUserService.list(Wrappers
                                                                                                 .<AlarmRecordHandlerUserPO>lambdaQuery()
                                                                                                 .eq(AlarmRecordHandlerUserPO::getAlarmRecordId, id));
        List<AlarmHandlerUser> alarmHandlerUsers = new ArrayList<>(userPOS.size());
        userPOS.forEach(userPO -> {
            alarmHandlerUsers.add(new AlarmHandlerUser(userPO.getUserId(), userPO.getUserName(), userPO.getDepartmentId(), userPO
                    .getDepartmentName()));
        });
        //报警处理原因信息
        List<AlarmRecordHandlerReasonPO> alarmRecordHandlerReasonPOS = this.alarmRecordHandlerReasonMapper.selectList(Wrappers
                                                                                                                              .<AlarmRecordHandlerReasonPO>lambdaQuery()
                                                                                                                              .eq(AlarmRecordHandlerReasonPO::getAlarmRecordId, id)
                                                                                                                     );
        AlarmReason alarmReason = null;
        if (CollectionUtils.isNotEmpty(alarmRecordHandlerReasonPOS)) {
            alarmReason = new AlarmReason();
            AlarmRecordHandlerReasonPO alarmRecordHandlerReasonPO = alarmRecordHandlerReasonPOS.get(0);
            alarmReason.setId(alarmRecordHandlerReasonPO.getAlarmReasonId());
            alarmReason.setCode(alarmRecordHandlerReasonPO.getAlarmReasonCode());
            alarmReason.setReason(alarmRecordHandlerReasonPO.getAlarmReason());
            alarmReason.setRemark(alarmRecordHandlerReasonPO.getAlarmReasonRemark());
        }
        List<String> channelTypes = new ArrayList<>();
        AlarmRecordNotifyChannelPO notifyChannelPO = this.alarmRecordNotifyChannelService.getByAlarmRecordId(id);
        if (Objects.nonNull(notifyChannelPO) && StringUtils.isNotBlank(notifyChannelPO.getChannelTypes())) {
            channelTypes = Arrays.asList(notifyChannelPO.getChannelTypes().split(","));
        }
        AlarmPushConfig alarmPushConfig = null;
        AlarmRecordPushConfigPO alarmRecordPushConfigPO = this.alarmRecordPushConfigService.getAlarmRecordPushConfigPO(id);
        if (Objects.nonNull(alarmRecordPushConfigPO)) {
            alarmPushConfig = new AlarmPushConfig(alarmRecordPO.getFilterScope(), alarmRecordPushConfigPO.getTurnWarningSound());
        }
        return AlarmRecord.builder()
                .alarmInfo(new AlarmInfo(alarmRecordPO.getId(), alarmRecordPO.getCode(), alarmRecordPO
                        .getAlarmEvent(), alarmRecordPO.getAlarmEventName(), alarmRecordPO
                                                 .getAlarmThreshold(), alarmRecordPO.getAlarmTime()))
                .alarmEventType(new AlarmEventType(alarmRecordPO.getAlarmEventTypeId(), alarmRecordPO
                        .getAlarmEventTypeCode(), alarmRecordPO.getAlarmEventTypeName()))
                .businessKey(alarmRecordPO.getBusinessKey())
                .businessType(alarmRecordPO.getBusinessType())
                .ruleId(alarmRecordPO.getRuleId())
                .alarmLevel(new AlarmLevel(alarmRecordPO.getAlarmLevelId(), alarmRecordPO
                        .getAlarmLevelCode(), alarmRecordPO.getAlarmLevelName(), alarmRecordPO.getAlarmLevelSort()))
                .alarmType(new AlarmType(alarmRecordPO.getAlarmTypeId(), alarmRecordPO.getAlarmTypeCode(), alarmRecordPO
                        .getAlarmTypeName(), alarmRecordPO.getApplicationId(), alarmRecordPO
                                                 .getApplicationCode(), alarmRecordPO.getApplicationName()))
                .alarmTarget(new AlarmTarget(alarmRecordPO.getAlarmTargetKey(), alarmRecordPO
                        .getAlarmTargetCode(), alarmRecordPO.getAlarmTargetName(), alarmRecordPO
                                                     .getAlarmTargetTypeId(), alarmRecordPO
                                                     .getAlarmTargetTypeName(), alarmRecordPO
                                                     .getAlarmTargetLongitude(), alarmRecordPO.getAlarmTargetAddress()))
                .area(new Area(alarmRecordPO.getArea(), alarmRecordPO.getAreaName()))
                .alarmStatus(new AlarmStatus(alarmRecordPO.getAlarmStatus(), alarmRecordPO.getAlarmStatusOrder()))
                .alarmConfirmed(new AlarmConfirmed(alarmRecordPO.getAlarmConfirmedBy(), alarmRecordPO
                        .getAlarmConfirmedName(), alarmRecordPO.getAlarmConfirmedTime()))
                .alarmSuspend(new AlarmSuspend(alarmRecordPO.getSuspendTime(), alarmRecordPO.getEstimateHandlerTime()))
                .alarmHandler(new AlarmHandler(alarmRecordPO.getAlarmHandlerBy(), alarmRecordPO
                        .getAlarmHandlerName(), alarmRecordPO.getAlarmHandlerTime(), alarmRecordPO
                                                       .getAlarmHandlerResult(), alarmReason, alarmRecordPO
                                                       .getAlarmHandlerResultRemark(), alarmRecordPO
                                                       .getProcessingTime()))
                .alarmTimeoutProcess(new AlarmTimeoutProcess(alarmRecordPO.getTimeoutEquipped(), alarmRecordPO
                        .getTimeoutProcessTime(), alarmRecordPO.getTimeoutProcessTimes(), alarmRecordPO
                                                                     .getTimeoutProcessTimesUnit()))
                .alarmSnapshot(new AlarmSnapshot(alarmRecordPO.getSnapshot(), alarmRecordPO.getAlarmSnapshot(), alarmRecordPO
                        .getBusinessSnapshot()))
                .alarmFilterScope(new AlarmFilterScope(alarmRecordPO.getFilterScope()))
                .alarmHandlerUsers(alarmHandlerUsers)
                .channelTypes(channelTypes)
                .alarmPushConfig(alarmPushConfig)
                .build();
    }

    public AlarmRecord detail(String businessKey) {
        AlarmRecordPO recordPO = this.alarmRecordMapper.selectOne(Wrappers.<AlarmRecordPO>lambdaQuery()
                                                                          .eq(AlarmRecordPO::getBusinessKey, businessKey));
        if (Objects.isNull(recordPO)) {
            return null;
        }
        return detail(recordPO.getId());
    }

    /**
     * 报警记录删除
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public List<AlarmRecordPO> remove(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<AlarmRecordPO> alarmRecordPOs = this.alarmRecordMapper.selectList(Wrappers.<AlarmRecordPO>lambdaQuery()
                                                                                       .in(AlarmRecordPO::getId, ids));
        DatabaseContextHolder.softDelete(AlarmRecordPO.class, alarmRecordPOs);
        return alarmRecordPOs;
    }
}
