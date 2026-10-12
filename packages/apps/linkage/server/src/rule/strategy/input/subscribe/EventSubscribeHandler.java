package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.subscribe;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.service.RuleDeviceAlarmService;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final RuleService ruleService;

    private final RuleBusinessDynamicHandler ruleBusinessDynamicHandler;

    private final RuleDeviceAlarmService ruleDeviceAlarmService;

    @EventListener
    public void subscribeDeviceEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceEventMsgHandler((message) -> {
            try {
                //符合条件的事件
                List<RuleDeviceAlarm> ruleDeviceAlarms = ruleDeviceAlarmService.list(Wrappers.<RuleDeviceAlarm>lambdaQuery()
                                                                                             .eq(RuleDeviceAlarm::getEventId, message.getEventCode()));
                if (CollectionUtils.isEmpty(ruleDeviceAlarms)) {
                    return;
                }
                LocalDateTime triggerTime = DateUtils.parse(message.getTimestamp());
                List<Long> ruleIds = ruleDeviceAlarms.stream().map(ruleDeviceAlarm -> ruleDeviceAlarm.getRuleId())
                        .collect(Collectors.toList());
                //查询规则列表
                List<Rule> rules = this.ruleService.listEnabledRulesByIds(ruleIds);
                rules.parallelStream().forEach(rule -> {
                    if (Objects.isNull(rule) || rule.getEnabled()) {
                        RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
                        request.setRule(rule);
                        request.setTriggerTime(triggerTime);
                        request.setTriggerJson(JsonUtils.serialize(message));
                        this.ruleBusinessDynamicHandler.ruleEvent(request);
                    }
                });
            } catch (Exception e) {
                log.error("事件触发后,获取规则失败,{}", e);
            }
        });
    }
}
