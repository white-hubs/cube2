package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.alarm.client.SatAlarmTypeClient;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.base.alarm.client.dto.SatAlarmTypeDTO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.ChangeRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DelRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DeviceProEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.GetDeviceAttributeRunningDetailEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.IncreaseRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.GetDeviceAttributeRunningRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.response.DeviceAttributeRunningResponse;
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
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleDeviceAlarmDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleDeviceAlarmEventDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dto.RuleIotTriggerSnapshot;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.enumeration.RuleConfigTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service.RuleDeviceAlarmService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.entity.RuleDeviceCondition;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.handler.RuleDeviceConditionHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service.RuleDeviceConditionService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory.DeviceProEventFactory;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.factory.RuleDeviceConditionFactory;
import com.guhecloud.cube.biz.linkage.server.util.DateUnitUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.NonNull;
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

/**
 * 设备报警规则解析器
 *
 * @author lanchun
 * @date 2022/6/22 10:05
 */
@Slf4j
@Component
public class RuleBusinessDeviceAlarmHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RuleDeviceAlarmService ruleDeviceAlarmService;

    @Autowired
    private RuleDeviceConditionService ruleDeviceConditionService;

    @Autowired
    private EventTriggerService eventTriggerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleDeviceConditionHandler ruleDeviceConditionHandler;

    @Autowired
    private SatAlarmTypeClient satAlarmTypeClient;

    @Autowired
    private SatAlarmLevelClient satAlarmLevelClient;

    public RuleBusinessDeviceAlarmHandler() {
        super(BusinessTypeEnum.DEVICE_ALARM);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleAnalysis(@NonNull RuleBusinessIncreaseHandlerRequest request) {
        //获取规则信息
        Rule rule = request.getRule();
        RuleDeviceAlarmDTO ruleDeviceAlarmDTO = getRuleDeviceAlarmDTO(rule);
        //规则新增
        this.ruleDeviceAlarmService.save(this.buildRuleDeviceAlarm(rule, ruleDeviceAlarmDTO));
        //发布事件
        List<DeviceProEvent> deviceProEvents = new ArrayList<>();
        //规则设备新增
        this.ruleDeviceConditionService.saveBatch(this.buildRuleDeviceAlarmConfigs(rule, ruleDeviceAlarmDTO, deviceProEvents));
        if (RuleConfigTypeEnum.IOT_DEVICE_EVENT == ruleDeviceAlarmDTO.getType()) {
            return;
        }
        IncreaseRuleEvent ruleCreateRequest = new IncreaseRuleEvent(this, rule.getCode(), rule.getName(), "or", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        //发布规则和动作到物联网平台
        this.applicationEventPublisher.publishEvent(ruleCreateRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
        //获取设备报警规则配置
        List<RuleDeviceAlarm> oldRuleDeviceAlarms = this.ruleDeviceAlarmService.listByRuleId(request.getRule().getId());
        //删除设备报警规则配置
        this.ruleDeviceAlarmService.removeByRuleId(request.getRule().getId());
        //删除设备报警设备配置
        this.ruleDeviceConditionService.removeByRuleId(request.getRule().getId());
        //获取新规则信息
        Rule rule = request.getRule();
        RuleDeviceAlarmDTO ruleDeviceAlarmDTO = getRuleDeviceAlarmDTO(rule);
        List<DeviceProEvent> deviceProEvents = new ArrayList<>();
        //规则新增
        this.ruleDeviceAlarmService.save(this.buildRuleDeviceAlarm(rule, ruleDeviceAlarmDTO));
        //如果之前的旧规则为物联设备属性条件规则,但新规则已改为事件触发,则发布规则删除事件到物联平台
        if (!CollectionUtils.isEmpty(oldRuleDeviceAlarms) &&
            RuleConfigTypeEnum.IOT_DEVICE_PRO_CONDITION == oldRuleDeviceAlarms.get(0).getType() &&
            RuleConfigTypeEnum.IOT_DEVICE_EVENT == ruleDeviceAlarmDTO.getType()) {
            DelRuleEvent delRuleEvent = new DelRuleEvent(this, request.getRule().getCode());
            //发布规则删除到物联网平台
            this.applicationEventPublisher.publishEvent(delRuleEvent);
        }
        //规则设备新增
        this.ruleDeviceConditionService.saveBatch(this.buildRuleDeviceAlarmConfigs(rule, ruleDeviceAlarmDTO, deviceProEvents));
        if (RuleConfigTypeEnum.IOT_DEVICE_EVENT == ruleDeviceAlarmDTO.getType()) {
            return;
        }
        //发布规则和动作到物联网平台
        ChangeRuleEvent changeRuleEvent = new ChangeRuleEvent(this, request.getOldCode(), rule.getCode(), rule.getName(), "or", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        this.applicationEventPublisher.publishEvent(changeRuleEvent);
    }

    private RuleDeviceAlarmDTO getRuleDeviceAlarmDTO(Rule rule) {
        SatAlarmTypeDTO satAlarmTypeDTO = satAlarmTypeClient.getAlarmType(rule.getTriggerTypeId());
        if (Objects.nonNull(satAlarmTypeDTO)) {
            rule.setTriggerTypeName(satAlarmTypeDTO.getName());
        }
        SatAlarmLevelDTO satAlarmLevelDTO = satAlarmLevelClient.getAlarmLevel(rule.getTriggerLevelId());
        if (Objects.nonNull(satAlarmLevelDTO)) {
            rule.setTriggerLevelName(satAlarmLevelDTO.getName());
        }
        this.ruleService.updateById(rule);
        //获取新规则json
        String ruleConfigStr = rule.getRuleConfig();
        //解析新规则
        return JsonUtils.deserialize(ruleConfigStr, RuleDeviceAlarmDTO.class);
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {
        //获取新规则信息
        Rule rule = request.getRule();
        //获取新规则json
        String ruleConfigStr = rule.getRuleConfig();
        //解析新规则
        RuleDeviceAlarmDTO ruleDeviceAlarmDTO = JsonUtils.deserialize(ruleConfigStr, RuleDeviceAlarmDTO.class);
        List<DeviceProEvent> deviceProEvents = new ArrayList<>();
        this.buildRuleDeviceAlarmConfigs(rule, ruleDeviceAlarmDTO, deviceProEvents);
        //发布规则和动作到物联网平台
        ChangeRuleEvent changeRuleEvent = new ChangeRuleEvent(this, request.getOldCode(), rule.getCode(), rule.getName(), "or", rule.getEnabled(), deviceProEvents, Collections.emptyList());
        this.applicationEventPublisher.publishEvent(changeRuleEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
        //删除设备报警规则配置
        this.ruleDeviceAlarmService.removeByRuleId(request.getRuleId());
        //删除设备条件配置
        this.ruleDeviceConditionHandler.ruleRemove(request);
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        SessionContextHolder.requestTenant(request.getRule().getTenant());
        if (BusinessTypeEnum.DEVICE_ALARM != request.getRule().getBusinessType()) {
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
        String triggerSnapshot = "";
        String event = "";
        String eventName = "";
        if (callBackRequest.containsKey("eventCode")) {
            //查询事件信息
            Long eventId = Long.valueOf(callBackRequest.get("eventCode").toString());
            eventName = this.ruleDeviceAlarmService.getOne(Wrappers.<RuleDeviceAlarm>lambdaQuery().eq(RuleDeviceAlarm::getEventId, eventId))
                    .getEventName();
            triggerSnapshot = callBackRequest.get("snapshot").toString();
        } else if (callBackRequest.containsKey("ruleId")) {
            event = String.valueOf(request.getRule().getId());
            eventName = request.getRule().getName();
            triggerSnapshot = JsonUtils.serialize(callBackRequest.get("ruleTriggerSnapshot"));
        }
        //解析物联平台返回的事件
        List<RuleIotTriggerSnapshot> ruleIotTriggerSnapshots = JsonUtils.deserialize(triggerSnapshot, new TypeReference<List<RuleIotTriggerSnapshot>>() {
        });
        RuleIotTriggerSnapshot ruleIotTriggerSnapshot = ruleIotTriggerSnapshots.get(0);
        //查询触发设备信息
        RuleDeviceCondition ruleDeviceCondition = this.ruleDeviceConditionService.getOne(Wrappers.<RuleDeviceCondition>lambdaQuery()
                                                                                                 .eq(RuleDeviceCondition::getDeviceId, ruleIotTriggerSnapshot.getDeviceId())
                                                                                                 .eq(RuleDeviceCondition::getRuleId, request
                                                                                                         .getRule().getId()));
        //构造事件对象
        RuleDeviceAlarmEventDTO ruleDeviceAlarmEventDTO = RuleDeviceAlarmEventDTO
                .builder()
                .eventName(request.getRule().getName())
                .event(event)
                .eventName(eventName)
                .triggerLevelId(request.getRule().getTriggerLevelId())
                .triggerLevelName(request.getRule().getTriggerLevelName())
                .triggerTypeId(request.getRule().getTriggerTypeId())
                .triggerTypeName(request.getRule().getTriggerTypeName())
                .triggerTargetKey(ruleDeviceCondition.getDeviceId())
                .triggerTargetCode(ruleDeviceCondition.getDeviceCode())
                .triggerTargetName(ruleDeviceCondition.getDeviceName())
                .triggerTargetTypeId(ruleDeviceCondition.getDeviceProdCategoryCode())
                .triggerTargetTypeName(ruleDeviceCondition.getDeviceProdCategoryName())
                .triggerThreshold(ruleIotTriggerSnapshot.getPropValue())
                .triggerTime(DateUtils.format(request.getTriggerTime()))
                .snapshot(JsonUtils.serialize(this.getDeviceAttributeRunning(ruleDeviceCondition.getDeviceId())))
                .area(ruleDeviceCondition.getAreaId())
                .areaName(ruleDeviceCondition.getAreaName())
                .build();
        this.ruleService.addTriggerNum(request.getRule().getId());

        //生成事件
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), JsonUtils.serialize(ruleDeviceAlarmEventDTO));
    }

    public RuleDeviceAlarm buildRuleDeviceAlarm(Rule rule, RuleDeviceAlarmDTO ruleDeviceAlarmDTO) {
        return RuleDeviceAlarm.builder()
                .ruleId(rule.getId())
                .type(RuleConfigTypeEnum.IOT_DEVICE_PRO_CONDITION)
                .deviceProdCategoryCode(ruleDeviceAlarmDTO.getDeviceProdCategoryCode())
                .deviceProdCategoryCodeConcat(ruleDeviceAlarmDTO.getDeviceProdCategoryCodeConcat())
                .deviceProdCategoryName(ruleDeviceAlarmDTO.getDeviceProdCategoryName())
                .deviceProdId(ruleDeviceAlarmDTO.getDeviceProdId())
                .deviceProdName(ruleDeviceAlarmDTO.getDeviceProdName())
                .propName(ruleDeviceAlarmDTO.getPropName())
                .eventId(ruleDeviceAlarmDTO.getEventId())
                .oprand(ruleDeviceAlarmDTO.getOprand())
                .op(ruleDeviceAlarmDTO.getOp())
                .delayTime(ruleDeviceAlarmDTO.getDelayTime())
                .build();

    }

    private List<RuleDeviceCondition> buildRuleDeviceAlarmConfigs(Rule rule, RuleDeviceAlarmDTO ruleDeviceAlarmDTO,
                                                                  List<DeviceProEvent> deviceProEvents
                                                                 ) {
        //规则设备新增
        List<RuleDeviceCondition> ruleDeviceConditions = new ArrayList<>();
        ruleDeviceAlarmDTO.getRuleDeviceAlarmConfigDTOS().stream().forEach(ruleDeviceAlarmConfigDTO -> {
            RuleDeviceCondition ruleDeviceCondition = RuleDeviceConditionFactory.getRuleDeviceCondition(rule, ruleDeviceAlarmDTO, ruleDeviceAlarmConfigDTO);
            ruleDeviceConditions.add(ruleDeviceCondition);
            DeviceProEvent deviceProEvent = DeviceProEventFactory.getDeviceProEvent(ruleDeviceAlarmDTO, ruleDeviceAlarmConfigDTO);
            deviceProEvents.add(deviceProEvent);
        });
        return ruleDeviceConditions;
    }

    private List<DeviceAttributeRunningResponse> getDeviceAttributeRunning(Long deviceId) {
        GetDeviceAttributeRunningRequest param = new GetDeviceAttributeRunningRequest();
        param.setDeviceId(deviceId);
        //发布获取设备属性值事件
        GetDeviceAttributeRunningDetailEvent getDeviceAttributeRunningDetailEvent = new GetDeviceAttributeRunningDetailEvent(this, param);
        this.applicationEventPublisher.publishEvent(getDeviceAttributeRunningDetailEvent);
        return getDeviceAttributeRunningDetailEvent.get();
    }
}
