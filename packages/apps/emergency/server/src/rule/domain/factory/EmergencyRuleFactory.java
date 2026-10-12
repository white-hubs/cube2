package com.guhecloud.cube.biz.emergency.server.rule.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.domain.entity.EmergencyInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionDeviceInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionExpInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleTimeInfo;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyTypeInfo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class EmergencyRuleFactory {

    /**
     * 创建新预案规则实例
     *
     * @param increaseEmergencyRuleCommand
     *
     * @return
     */
    public static EmergencyRule newEmergencyRule(IncreaseEmergencyRuleCommand increaseEmergencyRuleCommand) {
        EmergencyTypeMapper emergencyTypeMapper = ApplicationContextHolder.getBean(EmergencyTypeMapper.class);
        //基本信息
        EmergencyInfo emergencyInfo = new EmergencyInfo(increaseEmergencyRuleCommand.getName(), increaseEmergencyRuleCommand.getAlarmLevelId(), increaseEmergencyRuleCommand.getRemark());
        //条件组
        List<EmergencyRuleConditionGroupInfo> emergencyRuleConditionGroupInfos = new ArrayList<>(increaseEmergencyRuleCommand
                                                                                                         .getEmergencyRuleConditionGroups()
                                                                                                         .size());
        increaseEmergencyRuleCommand.getEmergencyRuleConditionGroups().forEach(emergencyRuleConditionGroupCommand -> {
            //条件规则
            List<EmergencyRuleConditionInfo> emergencyRuleConditionInfos = new ArrayList<>(emergencyRuleConditionGroupCommand
                                                                                                   .getEmergencyRuleConditions().size());
            long groupId = IdWorker.getId();
            emergencyRuleConditionGroupCommand.getEmergencyRuleConditions().forEach(increaseEmergencyRuleConditionCommand -> {
                String[] prodCategoryCodes = increaseEmergencyRuleConditionCommand.getDeviceProdCategoryCodeConcat().split("-");
                EmergencyRuleConditionInfo emergencyRuleConditionInfo = new EmergencyRuleConditionInfo(
                        groupId,
                        prodCategoryCodes.length > 0 ? prodCategoryCodes[prodCategoryCodes.length - 1] : null,
                        increaseEmergencyRuleConditionCommand.getDeviceProdCategoryCodeConcat(),
                        increaseEmergencyRuleConditionCommand.getDeviceProdCategoryName(),
                        increaseEmergencyRuleConditionCommand.getDeviceProdId(),
                        increaseEmergencyRuleConditionCommand.getDeviceProdName(),
                        increaseEmergencyRuleConditionCommand.getConditionType(),
                        increaseEmergencyRuleConditionCommand.getDeviceEventId(),
                        increaseEmergencyRuleConditionCommand.getDeviceEventCode(),
                        increaseEmergencyRuleConditionCommand.getDeviceEventName(),
                        increaseEmergencyRuleConditionCommand.getDeviceEventType(),
                        increaseEmergencyRuleConditionCommand.getLogicalOp(),
                        increaseEmergencyRuleConditionCommand.getEventConfigType(),
                        ObjectUtils.copying(increaseEmergencyRuleConditionCommand.getEmergencyRuleConditionExps(), EmergencyRuleConditionExpInfo.class));
                emergencyRuleConditionInfos.add(emergencyRuleConditionInfo);
            });
            //时间段
            EmergencyRuleTimeInfo emergencyRuleTimeInfo = ObjectUtils.copying(emergencyRuleConditionGroupCommand.getEmergencyRuleTime(), EmergencyRuleTimeInfo.class);
            emergencyRuleTimeInfo.setId(IdWorker.getId());
            emergencyRuleConditionGroupInfos.add(new EmergencyRuleConditionGroupInfo(groupId, emergencyRuleConditionInfos, emergencyRuleTimeInfo));
        });
        //条件设备
        List<EmergencyRuleConditionDeviceInfo> emergencyRuleConditionDeviceInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(increaseEmergencyRuleCommand.getEmergencyRuleConditionDevices())) {
            increaseEmergencyRuleCommand.getEmergencyRuleConditionDevices().forEach(increaseEmergencyRuleConditionDeviceCommand -> {
                String[] prodCategoryCodes = increaseEmergencyRuleConditionDeviceCommand.getDeviceProdCategoryCodeConcat().split("-");
                EmergencyRuleConditionDeviceInfo emergencyRuleConditionDeviceInfo = new EmergencyRuleConditionDeviceInfo(
                        prodCategoryCodes.length > 0 ? prodCategoryCodes[prodCategoryCodes.length - 1] : null,
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceProdCategoryCodeConcat(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceProdCategoryName(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceProdId(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceProdName(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceId(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceName(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceAreaId(),
                        increaseEmergencyRuleConditionDeviceCommand.getDeviceAreaName(),
                        increaseEmergencyRuleConditionDeviceCommand.getEnabled());
                emergencyRuleConditionDeviceInfos.add(emergencyRuleConditionDeviceInfo);
            });
        }
        EmergencyType emergencyType = emergencyTypeMapper.selectOne(Wrappers.<EmergencyType>lambdaQuery()
                                                                            .eq(EmergencyType::getApplicationCode, increaseEmergencyRuleCommand.getApplicationCode()));
        //预案类型信息
        EmergencyTypeInfo emergencyTypeInfo = new EmergencyTypeInfo(emergencyType.getId(), emergencyType.getApplicationCode(), emergencyType.getApplicationName());
        EmergencyRule emergencyRule = new EmergencyRule(
                emergencyInfo,
                emergencyTypeInfo,
                increaseEmergencyRuleCommand.getEnabled(),
                increaseEmergencyRuleCommand.getLogicalOp(),
                increaseEmergencyRuleCommand.getAppointDevice(),
                emergencyRuleConditionGroupInfos,
                emergencyRuleConditionDeviceInfos
        );
        //事件分类信息
        emergencyRule.settingEmergencyEventType(increaseEmergencyRuleCommand.getEmergencyEventTypeCode());
        //去重信息
        emergencyRule.settingRepeatTimeInfo(increaseEmergencyRuleCommand.getRepeatTime(), increaseEmergencyRuleCommand.getRepeatTimeUnit());
        //超时信息
        emergencyRule.settingEstimatedHandlerTimeInfo(increaseEmergencyRuleCommand.getEstimatedHandlerTime(), increaseEmergencyRuleCommand.getEstimatedHandlerUnit());
        //指挥调度推送配置
        emergencyRule.settingEmergencyPushConfig(increaseEmergencyRuleCommand.getNotifyCommandDispatch(),
                                                 increaseEmergencyRuleCommand.getTurnWarningSound());
        return emergencyRule;
    }
}
