package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.ChangeRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DelRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DeviceProEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.IncreaseRuleEvent;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleIotTriggerSnapshot;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionEventDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.dto.RuleDeviceConditionEventInfoDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service.RuleDeviceConditionService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory.DeviceProEventFactory;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory.RuleDeviceConditionFactory;
import com.guhecloud.cube.biz.linkage.server.util.DateUnitUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 条件规则处理器
 *
 * @author lanchun
 * @date 2022/7/4 14:32
 */
@Slf4j
@Component
public class RuleDeviceConditionHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RuleDeviceConditionService ruleDeviceConditionService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTriggerService eventTriggerService;

    @Autowired
    private RuleService ruleService;

    public RuleDeviceConditionHandler() {
        super(BusinessTypeEnum.DEVICE_CONDITION);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleAnalysis(RuleBusinessIncreaseHandlerRequest request) {
        //获取规则信息
        Rule rule = request.getRule();
        //规则解析
        List<RuleDeviceConditionDTO> ruleDeviceConditionDTOS = JsonUtils.deserialize(rule.getRuleConfig(), new TypeReference<List<RuleDeviceConditionDTO>>() {
        });
        //新增设备规则条件并返回新增事件
        List<DeviceProEvent> deviceProEvents = addRuleDeviceCondition(rule, ruleDeviceConditionDTOS);
        IncreaseRuleEvent ruleCreateRequest = new IncreaseRuleEvent(this, rule.getCode(), rule.getName(), "and", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        //        //发布规则和动作到物联网平台
        this.applicationEventPublisher.publishEvent(ruleCreateRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
        //删除设备报警设备配置
        this.ruleDeviceConditionService.removeByRuleId(request.getRule().getId());
        //获取新规则信息
        Rule rule = request.getRule();
        //规则解析
        List<RuleDeviceConditionDTO> ruleDeviceConditionDTOS = JsonUtils.deserialize(rule.getRuleConfig(), new TypeReference<List<RuleDeviceConditionDTO>>() {
        });
        //新增设备规则条件并返回新增事件
        List<DeviceProEvent> deviceProEvents = addRuleDeviceCondition(rule, ruleDeviceConditionDTOS);
        //发布规则和动作修改到物联网平台
        ChangeRuleEvent changeRuleEvent = new ChangeRuleEvent(this, request.getOldCode(), rule.getCode(), rule.getName(), "and", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        this.applicationEventPublisher.publishEvent(changeRuleEvent);
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {
        //获取新规则信息
        Rule rule = request.getRule();
        //规则解析
        List<RuleDeviceConditionDTO> ruleDeviceConditionDTOS = JsonUtils.deserialize(rule.getRuleConfig(), new TypeReference<List<RuleDeviceConditionDTO>>() {
        });
        List<DeviceProEvent> deviceProEvents = new ArrayList<>();
        this.buildRuleDeviceAlarmConfigs(rule, ruleDeviceConditionDTOS, deviceProEvents);
        //发布规则和动作到物联网平台
        ChangeRuleEvent changeRuleEvent = new ChangeRuleEvent(this, request.getOldCode(), rule.getCode(), rule.getName(), "and", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        this.applicationEventPublisher.publishEvent(changeRuleEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
        //删除设备报警设备配置
        this.ruleDeviceConditionService.removeByRuleId(request.getRuleId());
        DelRuleEvent delRuleEvent = new DelRuleEvent(this, request.getRuleCode());
        //发布规则删除到物联网平台
        this.applicationEventPublisher.publishEvent(delRuleEvent);
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        SessionContextHolder.requestTenant(request.getRule().getTenant());
        if (BusinessTypeEnum.DEVICE_CONDITION != request.getRule().getBusinessType()) {
            return;
        }
        //配置了去重设置
        if (Objects.nonNull(request.getRule().getRepeatTime())) {
            LocalDateTime lastTime = DateUnitUtils.add(request.getTriggerTime(), request.getRule().getRepeatTimeUnit(), -request.getRule()
                    .getRepeatTime());
            //判断是否在一定周期内触发过事件
            long triggerCount = this.eventService.count(Wrappers.<Event>lambdaQuery()
                                                                .eq(Event::getRuleId, request.getRule().getId())
                                                                .gt(Event::getTriggerTime, lastTime)
                                                                .le(Event::getTriggerTime, request.getTriggerTime())
                                                       );
            if (triggerCount != 0) {
                return;
            }
        }
        Map callBackRequest = JsonUtils.deserialize(request.getTriggerJson(), Map.class);
        //触发对象
        String triggerSnapshot = JsonUtils.serialize(callBackRequest.get("ruleTriggerSnapshot"));
        //解析物联平台返回的事件
        List<RuleIotTriggerSnapshot> ruleIotTriggerSnapshots = JsonUtils.deserialize(triggerSnapshot, new TypeReference<List<RuleIotTriggerSnapshot>>() {
        });
        Set<String> deviceIds = ruleIotTriggerSnapshots.stream().map(ruleIotTriggerSnapshot -> ruleIotTriggerSnapshot.getDeviceId())
                .collect(Collectors.toSet());
        Map<String, RuleDeviceCondition> ruleDeviceConditions = this.ruleDeviceConditionService
                .list(Wrappers.<RuleDeviceCondition>lambdaQuery()
                              .eq(RuleDeviceCondition::getRuleId, request.getRule().getId())
                              .in(RuleDeviceCondition::getDeviceId, deviceIds))
                .stream()
                .collect(Collectors.toMap(RuleDeviceCondition::getDeviceCode, ruleDeviceCondition -> ruleDeviceCondition, (o, n) -> n));
        //触发事件详情
        List<RuleDeviceConditionEventDTO> ruleDeviceConditionEventDTOS = new ArrayList<>();
        ruleIotTriggerSnapshots.stream().forEach(ruleIotTriggerSnapshot -> {
            if (ruleDeviceConditions.containsKey(ruleIotTriggerSnapshot.getDeviceId())) {
                RuleDeviceCondition ruleDeviceCondition = ruleDeviceConditions.get(ruleIotTriggerSnapshot.getDeviceId());
                RuleDeviceConditionEventDTO ruleDeviceConditionEventDTO = RuleDeviceConditionEventDTO.builder()
                        .triggerLevelId(request.getRule().getTriggerLevelId())
                        .triggerTypeId(request.getRule().getTriggerTypeId())
                        .triggerTargetKey(ruleDeviceCondition.getDeviceId())
                        .triggerTargetCode(ruleDeviceCondition.getDeviceCode())
                        .triggerTargetName(ruleDeviceCondition.getDeviceName())
                        .triggerTargetTypeId(ruleDeviceCondition.getDeviceProdCategoryCode())
                        .triggerTargetTypeName(ruleDeviceCondition.getDeviceProdCategoryName())
                        .triggerThreshold(ruleIotTriggerSnapshot.getPropValue())
                        .propCode(ruleIotTriggerSnapshot.getPropCode())
                        .propName(ruleDeviceCondition.getPropName())
                        .propValueTs(ruleIotTriggerSnapshot.getPropValueTs())
                        .area(ruleDeviceCondition.getAreaId())
                        .areaName(ruleDeviceCondition.getAreaName())
                        .build();
                ruleDeviceConditionEventDTOS.add(ruleDeviceConditionEventDTO);
            }
        });
        this.ruleService.addTriggerNum(request.getRule().getId());
        RuleDeviceConditionEventInfoDTO ruleDeviceConditionEventInfoDTO = RuleDeviceConditionEventInfoDTO.builder()
                .eventName(request.getRule().getName())
                .triggerTime(DateUtils.format(request.getTriggerTime()))
                .ruleDeviceConditionEventDTOS(JsonUtils.serialize(ruleDeviceConditionEventDTOS))
                .build();
        //生成事件
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), JsonUtils.serialize(ruleDeviceConditionEventInfoDTO));
    }

    public List<DeviceProEvent> addRuleDeviceCondition(Rule rule, List<RuleDeviceConditionDTO> ruleDeviceConditionDTOS) {
        //发布事件
        List<DeviceProEvent> deviceProEvents = new ArrayList<>();
        //规则设备新增
        this.ruleDeviceConditionService.saveBatch(this.buildRuleDeviceAlarmConfigs(rule, ruleDeviceConditionDTOS, deviceProEvents));
        return deviceProEvents;
    }

    private List<RuleDeviceCondition> buildRuleDeviceAlarmConfigs(Rule rule, List<RuleDeviceConditionDTO> ruleDeviceConditionDTOS,
                                                                  List<DeviceProEvent> deviceProEvents
                                                                 ) {
        //规则设备新增
        List<RuleDeviceCondition> ruleDeviceConditions = new ArrayList<>();
        ruleDeviceConditionDTOS.stream().forEach(ruleDeviceAlarmConfigDTO -> {
            RuleDeviceCondition ruleDeviceCondition = RuleDeviceConditionFactory.getRuleDeviceCondition(rule, ruleDeviceAlarmConfigDTO);
            ruleDeviceConditions.add(ruleDeviceCondition);
            DeviceProEvent deviceProEvent = DeviceProEventFactory.getDeviceProEvent(ruleDeviceAlarmConfigDTO);
            deviceProEvents.add(deviceProEvent);
        });
        return ruleDeviceConditions;
    }
}
