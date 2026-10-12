package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.linelossalarm.handler;

import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.alarmnotify.handler.LinkageActionAlarmNotifyHandler;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.linelossalarm.dto.RuleLineLossEventDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

/**
 * 线损报警解析器
 */
@Slf4j
@Component
public class RuleBusinessLineLossHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private EventTriggerService eventTriggerService;

    @Autowired
    private LinkageActionAlarmNotifyHandler linkageActionAlarmNotifyHandler;

    public RuleBusinessLineLossHandler() {
        super(BusinessTypeEnum.LINE_LOSS_ALARM);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleAnalysis(@NonNull RuleBusinessIncreaseHandlerRequest request) {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        SessionContextHolder.requestTenant(request.getRule().getTenant());
        var dto = JsonUtils.deserialize(request.getTriggerJson(), RuleLineLossEventDTO.class);
        if (dto != null) {
            var event = this.eventTriggerService.addEventAndTriggerVideoActionOnly(
                    request.getRule(),
                    String.valueOf(dto.getTriggerTargetKey()),
                    request.getTriggerTime(),
                    Objects.requireNonNull(request.getTriggerJson()),
                    null,
                    dto.getSnapshot());
            linkageActionAlarmNotifyHandler.sendAlarmMsg(event, Collections.emptyList());
        }
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), request.getTriggerJson());
    }
}
