package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyDeviceNotifyRuleCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyDeviceNotifyRuleConditionCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyDeviceNotifyRuleConditionExpCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyMessageNotifyCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyConfig;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleCondition;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.DeviceNotifyRuleConditionExp;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.valueobj.MessageTargetNotify;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备通知规则转换工具类
 * 提供静态方法实现各类对象之间的转换
 */
public class DeviceNotifyRuleCommandTOEntityConvert {

    private DeviceNotifyRuleCommandTOEntityConvert() {}

    /**
     * 将ModifyDeviceNotifyRuleCommand转换为DeviceNotifyRule实体
     *
     * @param command 修改设备通知规则命令
     *
     * @return 设备通知规则实体
     */
    public static DeviceNotifyRule modifyDeviceNotifyRuleCommandToEntity(ModifyDeviceNotifyRuleCommand command) {
        if (command == null) {
            return null;
        }
        return DeviceNotifyRule.builder()
                .deviceId(command.getDeviceId())
                .iotRuleId(IdWorker.getId())
                .enabled(command.getEnabled())
                .conditions(modifyDeviceNotifyRuleConditionsCommandToEntities(command.getRuleConditions()))
                .notifyConfigs(modifyDeviceNotifyRuleCommandTOEntities(command.getMessageNotifies()))
                .build();
    }

    /**
     * 将ModifyDeviceNotifyRuleConditionCommand列表转换为DeviceNotifyRuleCondition实体列表
     *
     * @param commands 修改设备通知规则条件命令列表
     *
     * @return 设备通知规则条件实体列表
     */
    public static List<DeviceNotifyRuleCondition> modifyDeviceNotifyRuleConditionsCommandToEntities(
            List<ModifyDeviceNotifyRuleConditionCommand> commands
                                                                                                   ) {
        if (CollectionUtils.isEmpty(commands)) {
            return Collections.emptyList();
        }
        return commands.stream()
                .map(DeviceNotifyRuleCommandTOEntityConvert::modifyDeviceNotifyRuleConditionsCommandToEntity)
                .collect(Collectors.toList());
    }

    /**
     * 将ModifyDeviceNotifyRuleConditionCommand转换为DeviceNotifyRuleCondition实体
     *
     * @param command 修改设备通知规则条件命令
     *
     * @return 设备通知规则条件实体
     */
    public static DeviceNotifyRuleCondition modifyDeviceNotifyRuleConditionsCommandToEntity(ModifyDeviceNotifyRuleConditionCommand command
                                                                                           ) {
        if (command == null) {
            return null;
        }
        return DeviceNotifyRuleCondition.builder()
                .conditionType(command.getConditionType())
                .deviceEventId(command.getDeviceEventId())
                .deviceEventCode(command.getDeviceEventCode())
                .deviceEventName(command.getDeviceEventName())
                .deviceEventType(command.getDeviceEventType())
                .logicalOp(command.getLogicalOp())
                .conditionExps(modifyDeviceNotifyRuleConditionsExpCommandToEntities(command.getRuleConditionExps()))
                .build();
    }

    /**
     * 将ModifyDeviceNotifyRuleConditionExpCommand列表转换为DeviceNotifyRuleConditionExp实体列表
     *
     * @param commands 修改设备通知规则条件表达式命令列表
     *
     * @return 设备通知规则条件表达式实体列表
     */
    public static List<DeviceNotifyRuleConditionExp> modifyDeviceNotifyRuleConditionsExpCommandToEntities(
            List<ModifyDeviceNotifyRuleConditionExpCommand> commands
                                                                                                         ) {
        if (CollectionUtils.isEmpty(commands)) {
            return Collections.emptyList();
        }
        return commands.stream()
                .map(DeviceNotifyRuleCommandTOEntityConvert::modifyDeviceNotifyRuleConditionsExpCommandToEntity)
                .collect(Collectors.toList());
    }

    /**
     * 将ModifyDeviceNotifyRuleConditionExpCommand转换为DeviceNotifyRuleConditionExp实体
     *
     * @param command 修改设备通知规则条件表达式命令
     *
     * @return 设备通知规则条件表达式实体
     */
    public static DeviceNotifyRuleConditionExp modifyDeviceNotifyRuleConditionsExpCommandToEntity(
            ModifyDeviceNotifyRuleConditionExpCommand command
                                                                                                 ) {
        if (command == null) {
            return null;
        }

        return DeviceNotifyRuleConditionExp.builder()
                .propCode(command.getPropCode())
                .propName(command.getPropName())
                .propUnit(command.getPropUnit())
                .op(command.getOp())
                .oprand(command.getOprand())
                .delayTime(command.getDelayTime())
                .startDelayTime(command.getStartDelayTime())
                .endDelayTime(command.getEndDelayTime())
                .build();
    }

    public static List<DeviceNotifyConfig> modifyDeviceNotifyRuleCommandTOEntities(List<ModifyMessageNotifyCommand> commands) {
        if (CollectionUtils.isEmpty(commands)) {
            return Collections.emptyList();
        }
        return commands.stream().map(command -> DeviceNotifyConfig.builder()
                .channelTypes(command.getChannelTypes())
                .targets(command.getTargets() != null ?
                         command.getTargets().stream()
                                 .map(targets -> MessageTargetNotify.builder()
                                         .messageTargetNotify(targets.getMessageTargetNotify())
                                         .targetConfig(targets.getTargetConfig())
                                         .build())
                                 .collect(Collectors.toList()) :
                         Collections.emptyList())
                .build()).collect(Collectors.toList());
    }
}
