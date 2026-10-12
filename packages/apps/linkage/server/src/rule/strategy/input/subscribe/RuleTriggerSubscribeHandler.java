package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.subscribe;

import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleTriggerSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final RuleService ruleService;

    private final RuleBusinessDynamicHandler ruleBusinessDynamicHandler;

    @EventListener
    public void subscribeRuleTrigger(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerRuleTriggerEventMsgHandler((ruleTriggerMsg) -> {
            Rule rule = this.ruleService.getByCode(ruleTriggerMsg.getRuleNum());
            if (Objects.isNull(rule) || !rule.getEnabled()) {
                return;
            }
            LocalDateTime triggerTime = DateUtils.parse(ruleTriggerMsg.getDelayEventTime());
            RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
            request.setRule(rule);
            request.setTriggerTime(triggerTime);
            request.setTriggerJson(JsonUtils.serialize(ruleTriggerMsg));
            this.ruleBusinessDynamicHandler.ruleEvent(request);
        });
    }
}
