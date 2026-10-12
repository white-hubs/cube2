package com.guhecloud.cube.biz.linkage.server.rule.strategy;

import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessRequest;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleBusinessDynamicHandler implements ApplicationRunner, RuleBusinessHandler {

    @Autowired
    private ApplicationContext applicationContext;

    private List<RuleBusinessHandler> ruleBusinessHandlers = new ArrayList<>();

    @Override
    public boolean supports(RuleBusinessRequest request) {
        return false;
    }

    @Override
    public void ruleAnalysis(RuleBusinessIncreaseHandlerRequest request) {
        this.determineRuleBusinessHandler(request).ruleAnalysis(request);
    }

    @Override
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
        this.determineRuleBusinessHandler(request).ruleUpdate(request);
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {
        this.determineRuleBusinessHandler(request).ruleEnabledUpdate(request);
    }

    @Override
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
        this.determineRuleBusinessHandler(request).ruleRemove(request);
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        this.determineRuleBusinessHandler(request).ruleEvent(request);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        applicationContext.getBeansOfType(RuleBusinessHandler.class).forEach((k, v) -> ruleBusinessHandlers.add(v));
    }

    private RuleBusinessHandler determineRuleBusinessHandler(RuleBusinessRequest request) {
        return this.ruleBusinessHandlers.stream().filter(ruleBusinessHandler -> ruleBusinessHandler.supports(request)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("没有找到 input=" + request.getBusinessType() + " 的策略处理器"));
    }
}
