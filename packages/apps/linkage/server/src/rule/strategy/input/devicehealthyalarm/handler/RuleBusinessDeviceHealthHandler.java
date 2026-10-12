package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicehealthyalarm.handler;

import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 设备健康报警解析器
 *
 * @author lanchun
 * @date 2022/6/22 10:05
 */
@Slf4j
@Component
public class RuleBusinessDeviceHealthHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private EventTriggerService eventTriggerService;

    public RuleBusinessDeviceHealthHandler() {
        super(BusinessTypeEnum.DEVICE_HEALTH_ALARM);
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
        //生成事件
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), request.getTriggerJson());
    }
}
