package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionExpService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionGroupService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleDisableService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleTimeService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.domain.entity.EmergencyInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionGroupPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleDisablePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyEventTypeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyPushConfig;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyTypeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EstimatedHandlerTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.RepeatTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleRepository {

    private final EmergencyRuleMapper emergencyRuleMapper;

    private final EmergencyRuleConditionService emergencyRuleConditionService;

    private final EmergencyRuleConditionExpService emergencyRuleConditionExpService;

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;

    private final EmergencyRuleTimeService emergencyRuleTimeService;

    private final EmergencyRuleConditionGroupService emergencyRuleConditionGroupService;

    private final EmergencyRuleDisableService emergencyRuleDisableService;

    /**
     * 保存预案规则
     *
     * @param emergencyRule
     */
    @Transactional(rollbackFor = Exception.class)
    public EmergencyRule save(EmergencyRule emergencyRule) {
        boolean isExecuteSave = true;
        //预案规则不存在则新增,存在则修改
        EmergencyRulePO existEmergencyPO = this.emergencyRuleMapper.selectById(emergencyRule.getId());
        if (Objects.nonNull(existEmergencyPO)) {
            isExecuteSave = false;
        }
        EmergencyRulePO emergencyRulePO = EmergencyRulePO.
                builder()
                .id(emergencyRule.getId())
                .name(emergencyRule.getEmergencyInfo().getName())
                .alarmLevelId(emergencyRule.getEmergencyInfo().getAlarmLevelId())
                .emergencyEventTypeCode(emergencyRule.getEmergencyEventTypeInfo().getEmergencyEventTypeCode())
                .emergencyTypeId(emergencyRule.getEmergencyTypeInfo().getId())
                .enabled(emergencyRule.getEnabled())
                .appointDevice(emergencyRule.getAppointDevice())
                .repeatTime(emergencyRule.getRepeatTimeInfo().getRepeatTime())
                .repeatTimeUnit(emergencyRule.getRepeatTimeInfo().getRepeatTimeUnit())
                .estimatedHandlerTime(emergencyRule.getEstimatedHandlerTimeInfo().getEstimatedHandlerTime())
                .estimatedHandlerUnit(emergencyRule.getEstimatedHandlerTimeInfo().getEstimatedHandlerUnit())
                .logicalOp(emergencyRule.getLogicalOp())
                .notifyCommandDispatch(emergencyRule.getEmergencyPushConfig().getNotifyCommandDispatch())
                .turnWarningSound(emergencyRule.getEmergencyPushConfig().getTurnWarningSound())
                .applicationCode(emergencyRule.getEmergencyTypeInfo().getApplicationCode())
                .applicationName(emergencyRule.getEmergencyTypeInfo().getApplicationName())
                .remark(emergencyRule.getEmergencyInfo().getRemark())
                .deleted(false)
                .build();
        if (Objects.nonNull(existEmergencyPO)) {
            EntityAuditor.populateCreateAudit(emergencyRulePO, existEmergencyPO.getCreateBy(), existEmergencyPO.getCreatorId(),
                                              existEmergencyPO.getCreatorType(), existEmergencyPO.getCreateTime());
        }
        if (emergencyRule.getEnabled()) {
            //删除规则禁用配置
            emergencyRuleDisableService.removeByRuleId(emergencyRulePO.getId());
        }
        //删除预案条件组
        this.emergencyRuleConditionGroupService.remove(Wrappers.<EmergencyRuleConditionGroupPO>lambdaQuery()
                                                               .eq(EmergencyRuleConditionGroupPO::getEmergencyRuleId, emergencyRule.getId()));
        //删除预案关联条件
        this.emergencyRuleConditionService.remove(Wrappers.<EmergencyRuleConditionPO>lambdaQuery()
                                                          .eq(EmergencyRuleConditionPO::getEmergencyRuleId, emergencyRule.getId()));
        //删除预案关条件表达式
        this.emergencyRuleConditionExpService.remove(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                                                             .eq(EmergencyRuleConditionExpPO::getEmergencyRuleId, emergencyRule.getId()));
        //删除预案关联设备
        this.emergencyRuleConditionDeviceService.remove(Wrappers.<EmergencyRuleConditionDevicePO>lambdaQuery()
                                                                .eq(EmergencyRuleConditionDevicePO::getEmergencyRuleId, emergencyRule.getId()));
        //删除规则生效时段
        this.emergencyRuleTimeService.remove(Wrappers.<EmergencyRuleTimePO>lambdaQuery()
                                                     .eq(EmergencyRuleTimePO::getEmergencyRuleId, emergencyRulePO.getId())
                                            );
        //预案规则关联条件组
        if (!CollectionUtils.isEmpty(emergencyRule.getEmergencyRuleConditionGroupInfos())) {
            List<EmergencyRuleTimePO> emergencyRuleTimePOS = new ArrayList<>();
            List<EmergencyRuleConditionGroupPO> emergencyRuleConditionGroupInfos = new ArrayList<>(emergencyRule
                                                                                                           .getEmergencyRuleConditionGroupInfos()
                                                                                                           .size());
            List<EmergencyRuleConditionPO> emergencyRuleConditionPOS = new ArrayList<>();
            List<EmergencyRuleConditionExpPO> emergencyRuleConditionExpPOS = new ArrayList<>();
            emergencyRule.getEmergencyRuleConditionGroupInfos().forEach(emergencyRuleConditionGroupInfo -> {
                                                                            emergencyRuleConditionGroupInfo.getEmergencyRuleConditionInfos().forEach(emergencyRuleConditionInfo -> {
                                                                                EmergencyRuleConditionPO emergencyRuleConditionPO = ObjectUtils.copying(emergencyRuleConditionInfo, EmergencyRuleConditionPO.class);
                                                                                emergencyRuleConditionPO.setId(IdWorker.getId());
                                                                                emergencyRuleConditionPO.setEmergencyRuleId(emergencyRule.getId());
                                                                                //预案条件表达式
                                                                                if (!CollectionUtils.isEmpty(emergencyRuleConditionInfo.getEmergencyRuleConditionExps())) {
                                                                                    emergencyRuleConditionInfo.getEmergencyRuleConditionExps().forEach(emergencyRuleConditionExpInfo -> {
                                                                                        EmergencyRuleConditionExpPO emergencyRuleConditionExpPO = ObjectUtils.copying(emergencyRuleConditionExpInfo, EmergencyRuleConditionExpPO.class);
                                                                                        emergencyRuleConditionExpPO.setGroupId(emergencyRuleConditionGroupInfo.getId());
                                                                                        emergencyRuleConditionExpPO.setEmergencyRuleConditionId(emergencyRuleConditionPO.getId());
                                                                                        emergencyRuleConditionExpPO.setEmergencyRuleId(emergencyRule.getId());
                                                                                        emergencyRuleConditionExpPOS.add(emergencyRuleConditionExpPO);
                                                                                    });
                                                                                }
                                                                                emergencyRuleConditionPOS.add(emergencyRuleConditionPO);
                                                                            });
                                                                            EmergencyRuleTimePO emergencyRuleTimePO = ObjectUtils.copying(emergencyRuleConditionGroupInfo.getEmergencyRuleTimeInfo(), EmergencyRuleTimePO.class);
                                                                            emergencyRuleTimePO.setEmergencyRuleId(emergencyRule.getId());
                                                                            emergencyRuleTimePO.setGroupId(emergencyRuleConditionGroupInfo.getId());
                                                                            emergencyRuleTimePOS.add(emergencyRuleTimePO);
                                                                            emergencyRuleConditionGroupInfos.add(new EmergencyRuleConditionGroupPO(emergencyRuleConditionGroupInfo.getId(), emergencyRule.getId()));
                                                                        }
                                                                       );
            this.emergencyRuleConditionGroupService.saveBatch(emergencyRuleConditionGroupInfos);
            //新增预案条件
            this.emergencyRuleConditionService.saveBatch(emergencyRuleConditionPOS);
            if (!CollectionUtils.isEmpty(emergencyRuleConditionExpPOS)) {
                //新增预案条件表达式
                this.emergencyRuleConditionExpService.saveBatch(emergencyRuleConditionExpPOS);
            }
            //新增预案时段
            if (!CollectionUtils.isEmpty(emergencyRuleTimePOS)) {
                this.emergencyRuleTimeService.saveBatch(emergencyRuleTimePOS);
            }
        }
        //预案规则关联设备
        if (!CollectionUtils.isEmpty(emergencyRule.getEmergencyRuleConditionDeviceInfos())) {
            List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = new ArrayList<>(emergencyRule
                                                                                                           .getEmergencyRuleConditionDeviceInfos()
                                                                                                           .size());
            emergencyRule.getEmergencyRuleConditionDeviceInfos().forEach(emergencyRuleConditionDeviceInfo -> {
                EmergencyRuleConditionDevicePO emergencyRuleConditionDevicePO = ObjectUtils.copying(emergencyRuleConditionDeviceInfo, EmergencyRuleConditionDevicePO.class);
                emergencyRuleConditionDevicePO.setId(IdWorker.getId());
                emergencyRuleConditionDevicePO.setEmergencyRuleId(emergencyRule.getId());
                emergencyRuleConditionDevicePOS.add(emergencyRuleConditionDevicePO);
            });
            //新增预案关联设备
            this.emergencyRuleConditionDeviceService.saveBatch(emergencyRuleConditionDevicePOS);
        }
        if (isExecuteSave) {
            //新增
            this.emergencyRuleMapper.insert(emergencyRulePO);
        } else {
            //修改
            this.emergencyRuleMapper.updateById(emergencyRulePO);
        }
        return emergencyRule;
    }

    /**
     * 获取预案规则
     *
     * @param emergencyRuleId
     *
     * @return
     */
    public Optional<EmergencyRule> get(Long emergencyRuleId) {
        EmergencyRulePO emergencyRulePO = this.emergencyRuleMapper.selectById(emergencyRuleId);
        if (Objects.isNull(emergencyRulePO)) {
            return Optional.empty();
        }
        //查询关联的规则条件
        List<EmergencyRuleConditionPO> emergencyRuleConditionPOS = this.emergencyRuleConditionService.list(Wrappers
                                                                                                                   .<EmergencyRuleConditionPO>lambdaQuery()
                                                                                                                   .eq(EmergencyRuleConditionPO::getEmergencyRuleId, emergencyRuleId));
        //查询关联的规则条件表达式
        Map<Long, List<EmergencyRuleConditionExpPO>> emergencyRuleConditionExpPOS = this.emergencyRuleConditionExpService
                .list(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                              .eq(EmergencyRuleConditionExpPO::getEmergencyRuleId, emergencyRuleId)).stream()
                .collect(Collectors.groupingBy(EmergencyRuleConditionExpPO::getEmergencyRuleConditionId));
        List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos = new ArrayList<>(emergencyRuleConditionPOS.size());
        emergencyRuleConditionPOS.forEach(emergencyRuleConditionPO -> {
            EmergencyRuleConditionInfo emergencyRuleConditionInfo = new EmergencyRuleConditionInfo(
                    emergencyRuleConditionPO.getGroupId(),
                    emergencyRuleConditionPO.getDeviceProdCategoryCode(),
                    emergencyRuleConditionPO.getDeviceProdCategoryCodeConcat(),
                    emergencyRuleConditionPO.getDeviceProdCategoryName(),
                    emergencyRuleConditionPO.getDeviceProdId(),
                    emergencyRuleConditionPO.getDeviceProdName(),
                    emergencyRuleConditionPO.getConditionType(),
                    emergencyRuleConditionPO.getDeviceEventId(),
                    emergencyRuleConditionPO.getDeviceEventCode(),
                    emergencyRuleConditionPO.getDeviceEventName(),
                    emergencyRuleConditionPO.getDeviceEventType(),
                    emergencyRuleConditionPO.getLogicalOp(),
                    emergencyRuleConditionPO.getEventConfigType(),
                    null
            );
            if (emergencyRuleConditionExpPOS.containsKey(emergencyRuleConditionPO.getId())) {
                emergencyRuleConditionInfo.settingEmergencyRuleConditionExps(ObjectUtils.copying(emergencyRuleConditionExpPOS.get(emergencyRuleConditionPO.getId()), EmergencyRuleConditionExpInfo.class));
            }
            emergencyRuleConditionInfos.add(emergencyRuleConditionInfo);
        });
        List<EmergencyRuleConditionGroupInfo> emergencyRuleConditionGroupInfos = new ArrayList<>();
        Map<Long, List<EmergencyRuleConditionInfo>> ruleConditionGroupMap = emergencyRuleConditionInfos.stream()
                .filter(emergencyRuleConditionInfo -> Objects.nonNull(emergencyRuleConditionInfo.getGroupId()))
                .collect(Collectors.groupingBy(EmergencyRuleConditionInfo::getGroupId));
        if (!MapUtils.isEmpty(ruleConditionGroupMap)) {
            Set<Long> groupIds = ruleConditionGroupMap.keySet();
            //获取时间段配置
            Map<Long, EmergencyRuleTimePO> ruleGroupTimeMap = this.emergencyRuleTimeService.list(Wrappers.<EmergencyRuleTimePO>lambdaQuery()
                                                                                                         .in(EmergencyRuleTimePO::getGroupId, groupIds))
                    .stream()
                    .collect(Collectors.toMap(EmergencyRuleTimePO::getGroupId, s -> s));
            groupIds.forEach(groupId -> {
                EmergencyRuleTimePO emergencyRuleTimePO = ruleGroupTimeMap.get(groupId);
                EmergencyRuleTimeInfo emergencyRuleTimeInfo = ObjectUtils.copying(emergencyRuleTimePO, EmergencyRuleTimeInfo.class);
                emergencyRuleConditionGroupInfos.add(new EmergencyRuleConditionGroupInfo(groupId, ruleConditionGroupMap.get(groupId), emergencyRuleTimeInfo));
            });
        } else {
            emergencyRuleConditionGroupInfos.add(new EmergencyRuleConditionGroupInfo(IdWorker.getId(), emergencyRuleConditionInfos, null));
        }
        //查询关联的规则设备
        List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = this.emergencyRuleConditionDeviceService.list(Wrappers
                                                                                                                                     .<EmergencyRuleConditionDevicePO>lambdaQuery()
                                                                                                                                     .eq(EmergencyRuleConditionDevicePO::getEmergencyRuleId, emergencyRuleId));
        //查询规则禁用配置
        Integer emergencyRuleDisable = null;
        List<EmergencyRuleDisablePO> emergencyRuleDisables = this.emergencyRuleDisableService.getEmergencyRuleDisableListByRuleIds(Collections.singleton(emergencyRulePO.getId()));
        if (!CollectionUtils.isEmpty(emergencyRuleDisables)) {
            emergencyRuleDisable = emergencyRuleDisables.getFirst().getDuration();
        }
        EmergencyPushConfig emergencyDispatchConfig = new EmergencyPushConfig(emergencyRulePO.getNotifyCommandDispatch(), emergencyRulePO.getTurnWarningSound());
        return Optional.of(EmergencyRule.builder()
                                   .id(emergencyRulePO.getId())
                                   .emergencyInfo(new EmergencyInfo(emergencyRulePO.getName(), emergencyRulePO.getAlarmLevelId(), emergencyRulePO.getRemark()))
                                   .emergencyEventTypeInfo(new EmergencyEventTypeInfo(emergencyRulePO.getEmergencyEventTypeCode()))
                                   .emergencyTypeInfo(new EmergencyTypeInfo(emergencyRulePO.getEmergencyTypeId(), emergencyRulePO.getApplicationCode(), emergencyRulePO.getApplicationName()))
                                   .enabled(emergencyRulePO.getEnabled())
                                   .appointDevice(!Objects.isNull(emergencyRulePO.getAppointDevice()) && emergencyRulePO.getAppointDevice())
                                   .estimatedHandlerTimeInfo(new EstimatedHandlerTimeInfo(emergencyRulePO.getEstimatedHandlerTime(), emergencyRulePO.getEstimatedHandlerUnit()))
                                   .repeatTimeInfo(new RepeatTimeInfo(emergencyRulePO.getRepeatTime(), emergencyRulePO.getRepeatTimeUnit()))
                                   .logicalOp(emergencyRulePO.getLogicalOp())
                                   .appointDevice(Objects.isNull(emergencyRulePO.getAppointDevice())
                                                  ? !CollectionUtils.isEmpty(emergencyRuleConditionDevicePOS)
                                                  : emergencyRulePO.getAppointDevice())
                                   .emergencyRuleConditionGroupInfos(emergencyRuleConditionGroupInfos)
                                   .emergencyRuleConditionDeviceInfos(ObjectUtils.copying(emergencyRuleConditionDevicePOS, EmergencyRuleConditionDeviceInfo.class))
                                   .disableDuration(emergencyRuleDisable)
                                   .emergencyPushConfig(emergencyDispatchConfig)
                                   .build());
    }

    /**
     * 预案规则状态修改
     *
     * @param emergencyRuleId
     * @param enabled
     * @param disableDuration
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long emergencyRuleId, Boolean enabled, Integer disableDuration) {
        EmergencyRulePO emergencyRulePO = this.emergencyRuleMapper.selectById(emergencyRuleId);
        if (Objects.isNull(emergencyRulePO)) {
            return;
        }
        emergencyRuleDisableService.removeByRuleId(emergencyRuleId);
        if (!enabled && Objects.nonNull(disableDuration)) {
            EmergencyRuleDisablePO ruleDisablePO = EmergencyRuleDisablePO.builder()
                    .ruleId(emergencyRuleId)
                    .duration(disableDuration)
                    .openTime(DateUtils.offset(DateUtils.now(), ChronoUnit.HOURS, disableDuration))
                    .build();
            emergencyRuleDisableService.save(ruleDisablePO);
        }
        emergencyRulePO.setEnabled(enabled);
        this.emergencyRuleMapper.updateById(emergencyRulePO);
    }

    /**
     * 预案规则删除
     *
     * @param emergencyRuleId
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long emergencyRuleId) {
        this.deleteBatchIds(Collections.singleton(emergencyRuleId));
    }

    /**
     * 预案规则主键批量删除
     *
     * @param emergencyRuleIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(Set<Long> emergencyRuleIds) {
        DatabaseContextHolder.softDelete(EmergencyRulePO.class, emergencyRuleIds);
        //删除预案关联条件
        this.emergencyRuleConditionService.remove(Wrappers.<EmergencyRuleConditionPO>lambdaQuery()
                                                          .in(EmergencyRuleConditionPO::getEmergencyRuleId, emergencyRuleIds));
        //删除预案关条件表达式
        this.emergencyRuleConditionExpService.remove(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                                                             .in(EmergencyRuleConditionExpPO::getEmergencyRuleId, emergencyRuleIds));
        //删除预案关联设备
        this.emergencyRuleConditionDeviceService.remove(Wrappers.<EmergencyRuleConditionDevicePO>lambdaQuery()
                                                                .in(EmergencyRuleConditionDevicePO::getEmergencyRuleId, emergencyRuleIds));
        //删除预案生效时段配置
        this.emergencyRuleTimeService.remove(Wrappers.<EmergencyRuleTimePO>lambdaQuery()
                                                     .in(EmergencyRuleTimePO::getEmergencyRuleId, emergencyRuleIds)
                                            );
        //删除预案条件组
        this.emergencyRuleConditionGroupService.remove(Wrappers.<EmergencyRuleConditionGroupPO>lambdaQuery()
                                                               .in(EmergencyRuleConditionGroupPO::getEmergencyRuleId, emergencyRuleIds));
        //删除预案禁用配置
        emergencyRuleDisableService.removeByRuleIds(emergencyRuleIds);
    }
}
