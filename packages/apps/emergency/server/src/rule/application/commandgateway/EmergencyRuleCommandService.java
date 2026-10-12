package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway;

import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotRuleStateChangeEvent;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleEnabledCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.DeleteEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.ChangeEmergencyRuleValidator;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.IncreaseEmergencyRuleValidator;
import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import com.guhecloud.cube.biz.emergency.server.rule.domain.event.ChangeEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.rule.domain.event.DelEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.rule.domain.event.IncreaseEmergencyRuleEvent;
import com.guhecloud.cube.biz.emergency.server.rule.domain.factory.EmergencyRuleFactory;
import com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject.EmergencyRuleConditionGroupInfo;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.EmergencyRuleRepository;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyChangeMessage;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message.EmergencyIncreaseMessage;
import com.guhecloud.cube.biz.emergency.server.time.domain.event.DelIotRuleGroupEvent;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lanchun
 * @date 2022/9/3 14:45
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleCommandService {

    private final EmergencyRuleRepository emergencyRuleRepository;

    private final IncreaseEmergencyRuleValidator increaseEmergencyRuleValidator;

    private final ChangeEmergencyRuleValidator changeEmergencyRuleValidator;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 预案规则新增
     *
     * @param increaseEmergencyRuleCommand
     */
    public void increaseEmergencyRule(IncreaseEmergencyRuleCommand increaseEmergencyRuleCommand) {
        //校验
        this.increaseEmergencyRuleValidator.verify(increaseEmergencyRuleCommand);
        //构造预案规则对象
        EmergencyRule emergencyRule = EmergencyRuleFactory.newEmergencyRule(increaseEmergencyRuleCommand);
        //发送规则创建事件
        IncreaseEmergencyRuleEvent increaseEmergencyRuleEvent = new IncreaseEmergencyRuleEvent(
                this,
                emergencyRule,
                increaseEmergencyRuleCommand,
                emergencyRule.getId(),
                emergencyRule.getEmergencyInfo().getName());
        this.applicationEventPublisher.publishEvent(increaseEmergencyRuleEvent);
        EmergencyIncreaseMessage emergencyIncreaseMessage = new EmergencyIncreaseMessage();
        emergencyIncreaseMessage.setId(emergencyRule.getId());
        //发送规则创建事务消息
        RocketMQContextHolder.sendTransactionalMessage(EmergencyIncreaseMessage.TOPIC,
                                                       emergencyIncreaseMessage,
                                                       () -> {
                                                           //持久化
                                                           this.emergencyRuleRepository.save(emergencyRule);
                                                           return RocketMQLocalTransactionState.COMMIT;
                                                       });
    }

    /**
     * 预案规则修改
     *
     * @param changeEmergencyRuleCommand
     */
    public void changeEmergencyRule(ChangeEmergencyRuleCommand changeEmergencyRuleCommand) {
        //校验
        this.changeEmergencyRuleValidator.verify(changeEmergencyRuleCommand);
        EmergencyRule emergencyRule = this.emergencyRuleRepository.get(changeEmergencyRuleCommand.getId()).get();
        //修改基础信息
        emergencyRule.changeBasicInfo(changeEmergencyRuleCommand.getName(), changeEmergencyRuleCommand.getAlarmLevelId(), changeEmergencyRuleCommand.getLogicalOp(), changeEmergencyRuleCommand.getRemark());
        //修改超时配置信息
        emergencyRule.changeRepeatTimeInfo(changeEmergencyRuleCommand.getRepeatTime(), changeEmergencyRuleCommand.getRepeatTimeUnit());
        //修改超时处理配置信息
        emergencyRule.changeEstimatedHandlerTimeInfo(changeEmergencyRuleCommand.getEstimatedHandlerTime(), changeEmergencyRuleCommand.getEstimatedHandlerUnit());
        //修改启用信息
        emergencyRule.changeEnabled(changeEmergencyRuleCommand.getEnabled());
        //修改是否指定设备
        emergencyRule.changeAppointDevice(changeEmergencyRuleCommand.getAppointDevice());
        //修改预案类型信息
        emergencyRule.changeEmergencyTypeInfo(changeEmergencyRuleCommand.getApplicationCode());
        //修改条件信息
        emergencyRule.settingRuleConditionGroup(changeEmergencyRuleCommand.getEmergencyRuleConditionGroups());
        //修改条件设备
        emergencyRule.settingRuleConditionDevices(changeEmergencyRuleCommand.getEmergencyRuleConditionDevices());
        //修改事件分类
        emergencyRule.changeEmergencyEventType(changeEmergencyRuleCommand.getEmergencyEventTypeCode());
        //指挥调度推送配置
        emergencyRule.settingEmergencyPushConfig(changeEmergencyRuleCommand.getNotifyCommandDispatch(),
                                                 changeEmergencyRuleCommand.getTurnWarningSound());
        //持久化
        this.emergencyRuleRepository.save(emergencyRule);
        //发送规则修改事件
        ChangeEmergencyRuleEvent changeEmergencyRuleEvent = new ChangeEmergencyRuleEvent(
                this,
                emergencyRule,
                changeEmergencyRuleCommand,
                emergencyRule.getId(),
                emergencyRule.getEmergencyInfo().getName()
        );
        this.applicationEventPublisher.publishEvent(changeEmergencyRuleEvent);
        EmergencyChangeMessage emergencyChangeMessage = new EmergencyChangeMessage();
        emergencyChangeMessage.setId(emergencyRule.getId());
        //发送规则修改消息
        RocketMQContextHolder.send(EmergencyChangeMessage.TOPIC, emergencyChangeMessage);
    }

    /**
     * 预案规则启用状态修改
     *
     * @param changeEmergencyRuleEnabledCommand
     */
    public void changeEmergencyRuleEnabled(ChangeEmergencyRuleEnabledCommand changeEmergencyRuleEnabledCommand) {
        this.emergencyRuleRepository.updateEnabled(changeEmergencyRuleEnabledCommand.getId(),
                                                   changeEmergencyRuleEnabledCommand.getEnabled(),
                                                   changeEmergencyRuleEnabledCommand.getDisableDuration());
        Optional<EmergencyRule> emergencyRuleOptional = this.emergencyRuleRepository.get(changeEmergencyRuleEnabledCommand.getId());
        emergencyRuleOptional.ifPresent(emergencyRule -> {
            if (!CollectionUtils.isEmpty(emergencyRule.getEmergencyRuleConditionGroupInfos())) {
                List<Long> ids = emergencyRule.getEmergencyRuleConditionGroupInfos().stream().map(EmergencyRuleConditionGroupInfo::getId)
                        .collect(Collectors.toList());
                IotRuleStateChangeEvent iotRuleStateChangeEvent = new IotRuleStateChangeEvent(this, changeEmergencyRuleEnabledCommand.getEnabled(), ids);
                this.applicationEventPublisher.publishEvent(iotRuleStateChangeEvent);
            }
        });
    }

    /**
     * 预案规则删除
     *
     * @param deleteEmergencyRuleCommand
     */
    public void deleteEmergencyRule(DeleteEmergencyRuleCommand deleteEmergencyRuleCommand) {
        List<EmergencyRule> emergencyRules = new ArrayList<>();
        List<Long> ruleGroupIds = new ArrayList<>();
        deleteEmergencyRuleCommand.getEmergencyRuleIds().forEach(id -> {
            EmergencyRule emergencyRule = this.emergencyRuleRepository.get(id).get();
            Assert.isNotAcceptable(emergencyRule.getEnabled(), "预警规则正在使用中");
            emergencyRules.add(emergencyRule);
            if (!CollectionUtils.isEmpty(emergencyRule.getEmergencyRuleConditionGroupInfos())) {
                ruleGroupIds.addAll(emergencyRule.getEmergencyRuleConditionGroupInfos().stream().map(EmergencyRuleConditionGroupInfo::getId)
                                            .toList());
            }
        });
        this.emergencyRuleRepository.deleteBatchIds(deleteEmergencyRuleCommand.getEmergencyRuleIds());
        //规则删除事件发送
        this.applicationEventPublisher.publishEvent(new DelEmergencyRuleEvent(this, emergencyRules, deleteEmergencyRuleCommand.getEmergencyRuleIds()));
        //调用iot删除规则
        this.applicationEventPublisher.publishEvent(new DelIotRuleGroupEvent(this, ruleGroupIds));
    }
}
